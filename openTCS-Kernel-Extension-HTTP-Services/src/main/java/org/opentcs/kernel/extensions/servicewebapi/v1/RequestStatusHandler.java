/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.opentcs.kernel.extensions.servicewebapi.v1;

import java.util.HashSet;
import java.util.List;
import static java.util.Objects.requireNonNull;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import org.opentcs.access.KernelRuntimeException;
import org.opentcs.components.kernel.services.PeripheralService;
import org.opentcs.components.kernel.services.TransportOrderService;
import org.opentcs.components.kernel.services.VehicleService;
import org.opentcs.customizations.kernel.KernelExecutor;
import org.opentcs.data.ObjectUnknownException;
import org.opentcs.data.model.Location;
import org.opentcs.data.model.Vehicle;
import org.opentcs.data.order.OrderSequence;
import org.opentcs.data.order.TransportOrder;
import org.opentcs.data.peripherals.PeripheralJob;
import org.opentcs.drivers.peripherals.PeripheralCommAdapterDescription;
import org.opentcs.drivers.peripherals.management.PeripheralAttachmentInformation;
import org.opentcs.drivers.vehicle.VehicleCommAdapterDescription;
import org.opentcs.drivers.vehicle.management.AttachmentInformation;
import org.opentcs.kernel.extensions.servicewebapi.v1.binding.GetOrderSequenceResponseTO;
import org.opentcs.kernel.extensions.servicewebapi.v1.binding.GetPeripheralJobResponseTO;
import org.opentcs.kernel.extensions.servicewebapi.v1.binding.GetTransportOrderResponseTO;
import org.opentcs.kernel.extensions.servicewebapi.v1.binding.GetVehicleResponseTO;
import org.opentcs.kernel.extensions.servicewebapi.v1.binding.PutVehicleAllowedOrderTypesTO;
import org.opentcs.kernel.extensions.servicewebapi.v1.filter.OrderSequenceFilter;
import org.opentcs.kernel.extensions.servicewebapi.v1.filter.PeripheralJobFilter;
import org.opentcs.kernel.extensions.servicewebapi.v1.filter.TransportOrderFilter;
import org.opentcs.kernel.extensions.servicewebapi.v1.filter.VehicleFilter;

/**
 * Handles requests for getting the current state of model elements.
 *
 * @author Mustafa Yalciner (Fraunhofer IML)
 */
public class RequestStatusHandler {

  /**
   * The service used to manage peripherals.
   */
  private final PeripheralService peripheralService;
  /**
   * The service we use to fetch objects.
   */
  private final TransportOrderService orderService;
  /**
   * Used to update vehicle instances.
   */
  private final VehicleService vehicleService;
  /**
   * The kernel's executor service.
   */
  private final ExecutorService kernelExecutor;

  /**
   * Creates a new instance.
   *
   * @param peripheralService The service used to manage peripherals.
   * @param orderService The service we use to get the transport orders.
   * @param vehicleService Used to update vehicle instances.
   * @param kernelExecutor The kernel's executor service.
   */
  @Inject
  public RequestStatusHandler(PeripheralService peripheralService,
                              TransportOrderService orderService,
                              VehicleService vehicleService,
                              @KernelExecutor ExecutorService kernelExecutor) {
    this.peripheralService = requireNonNull(peripheralService, "peripheralService");
    this.orderService = requireNonNull(orderService, "orderService");
    this.vehicleService = requireNonNull(vehicleService, "vehicleService");
    this.kernelExecutor = requireNonNull(kernelExecutor, "kernelExecutor");
  }

  /**
   * Find all transport orders and filters depending on the given parameters.
   *
   * @param intendedVehicle The filter parameter for the name of the
   * intended vehicle for the transport order. The filtering is disabled for this parameter if the
   * value is null.
   * @return A list of transport orders that match the filter.
   */
  public List<GetTransportOrderResponseTO> getTransportOrdersState(
      @Nullable String intendedVehicle
  ) {
    if (intendedVehicle != null) {
      Vehicle vehicle = orderService.fetchObject(Vehicle.class, intendedVehicle);
      if (vehicle == null) {
        throw new ObjectUnknownException("Unknown vehicle: " + intendedVehicle);
      }
    }

    return orderService.fetchObjects(TransportOrder.class,
                                     new TransportOrderFilter(intendedVehicle))
        .stream()
        .map(order -> GetTransportOrderResponseTO.fromTransportOrder(order))
        .collect(Collectors.toList());
  }

  /**
   * Finds the transport order with the given name.
   *
   * @param name The name of the requested transport order.
   * @return A single transport order with the given name.
   * @throws ObjectUnknownException If a transport order with the given name does not exist.
   */
  public GetTransportOrderResponseTO getTransportOrderByName(String name)
      throws ObjectUnknownException {
    requireNonNull(name, "name");

    return orderService.fetchObjects(TransportOrder.class,
                                     t -> t.getName().equals(name))
        .stream()
        .map(t -> GetTransportOrderResponseTO.fromTransportOrder(t))
        .findAny()
        .orElseThrow(() -> new ObjectUnknownException("Unknown transport order: " + name));
  }

  public void putPeripheralCommAdapter(String name, String value)
      throws ObjectUnknownException {
    requireNonNull(name, "name");
    requireNonNull(value, "value");

    Location location = peripheralService.fetchObject(Location.class, name);
    if (location == null) {
      throw new ObjectUnknownException("Unknown location: " + name);
    }

    PeripheralCommAdapterDescription newAdapter
        = peripheralService.fetchAttachmentInformation(location.getReference())
            .getAvailableCommAdapters()
            .stream()
            .filter(description -> description.getClass().getName().equals(value))
            .findAny()
            .orElseThrow(
                () -> new IllegalArgumentException("Unknown peripheral driver class name: " + value)
            );

    try {
      kernelExecutor.submit(
          () -> peripheralService.attachCommAdapter(location.getReference(), newAdapter)
      ).get();
    }
    catch (InterruptedException exc) {
      throw new IllegalStateException("Unexpectedly interrupted");
    }
    catch (ExecutionException exc) {
      if (exc.getCause() instanceof RuntimeException) {
        throw (RuntimeException) exc.getCause();
      }
      throw new KernelRuntimeException(exc.getCause());
    }
  }

  public void putPeripheralCommAdapterEnabled(String name, String value)
      throws ObjectUnknownException, IllegalArgumentException {
    requireNonNull(name, "name");
    requireNonNull(value, "value");

    Location location = peripheralService.fetchObject(Location.class, name);
    if (location == null) {
      throw new ObjectUnknownException("Unknown location: " + name);
    }

    if (Boolean.parseBoolean(value)) {
      kernelExecutor.submit(
          () -> peripheralService.enableCommAdapter(location.getReference())
      );
    }
    else {
      kernelExecutor.submit(
          () -> peripheralService.disableCommAdapter(location.getReference())
      );
    }
  }

  public PeripheralAttachmentInformation getPeripheralCommAdapterAttachmentInformation(String name)
      throws ObjectUnknownException {
    requireNonNull(name, "name");

    Location location = peripheralService.fetchObject(Location.class, name);
    if (location == null) {
      throw new ObjectUnknownException("Unknown location: " + name);
    }

    return peripheralService.fetchAttachmentInformation(location.getReference());
  }

  /**
   * Returns all peripheral jobs, optionally filtered using the given parameters.
   *
   * @param relatedVehicle Which vehicle to filter peripheral jobs for. Not filtered if the value is
   * null.
   * @param relatedTransportOrder Which transport order to filter peripheral jobs for. Not filtered
   * if the value is null.
   * @return List of peripheral job states.
   */
  public List<GetPeripheralJobResponseTO> getPeripheralJobs(
      @Nullable String relatedVehicle,
      @Nullable String relatedTransportOrder
  ) {
    // If a related vehicle or transport order is set, make sure they exist.
    if (relatedVehicle != null && orderService.fetchObject(Vehicle.class, relatedVehicle) == null) {
      throw new ObjectUnknownException("Unknown vehicle: " + relatedVehicle);
    }
    if (relatedTransportOrder != null
        && orderService.fetchObject(TransportOrder.class, relatedTransportOrder) == null) {
      throw new ObjectUnknownException("Unknown transport order: " + relatedTransportOrder);
    }

    return orderService.fetchObjects(PeripheralJob.class,
                                     new PeripheralJobFilter(relatedVehicle, relatedTransportOrder))
        .stream()
        .map(peripheralJob -> GetPeripheralJobResponseTO.fromPeripheralJob(peripheralJob))
        .collect(Collectors.toList());
  }

  /**
   * Find a peripheral job by name.
   *
   * @param name The name of the peripheral job.
   * @return The peripheral job state.
   */
  public GetPeripheralJobResponseTO getPeripheralJobByName(@Nonnull String name) {
    requireNonNull(name, "name");

    PeripheralJob job = orderService.fetchObject(PeripheralJob.class, name);
    if (job == null) {
      throw new ObjectUnknownException("Unknown peripheral job: " + name);
    }

    return GetPeripheralJobResponseTO.fromPeripheralJob(job);
  }

  /**
   * Find all vehicles orders and filters depending on the given parameters.
   *
   * @param procState The filter parameter for the processing state of the vehicle.
   * The filtering is disabled for this parameter if the value is null.
   * @return A list of vehicles, that match the filter.
   */
  public List<GetVehicleResponseTO> getVehiclesState(@Nullable String procState) {
    List<GetVehicleResponseTO> vehicles = orderService.fetchObjects(Vehicle.class,
                                                                    new VehicleFilter(procState))
        .stream()
        .map(vehicle -> GetVehicleResponseTO.fromVehicle(vehicle))
        .collect(Collectors.toList());
    return vehicles;
  }

  /**
   * Finds the vehicle with the given name.
   *
   * @param name The name of the requested vehicle.
   * @return A single vehicle that has the given name.
   * @throws ObjectUnknownException If a vehicle with the given name does not exist.
   */
  public GetVehicleResponseTO getVehicleStateByName(String name)
      throws ObjectUnknownException {
    requireNonNull(name, "name");

    return orderService.fetchObjects(Vehicle.class, v -> v.getName().equals(name))
        .stream()
        .map(v -> GetVehicleResponseTO.fromVehicle(v))
        .findAny()
        .orElseThrow(() -> new ObjectUnknownException("Unknown vehicle: " + name));
  }

  public void putVehicleIntegrationLevel(String name, String value)
      throws ObjectUnknownException, IllegalArgumentException {
    requireNonNull(name, "name");
    requireNonNull(value, "value");

    Vehicle vehicle = orderService.fetchObject(Vehicle.class, name);
    if (vehicle == null) {
      throw new ObjectUnknownException("Unknown vehicle: " + name);
    }

    Vehicle.IntegrationLevel level = Vehicle.IntegrationLevel.valueOf(value);

    kernelExecutor.submit(
        () -> vehicleService.updateVehicleIntegrationLevel(vehicle.getReference(), level)
    );
  }

  public void putVehiclePaused(String name, String value)
      throws ObjectUnknownException, IllegalArgumentException {
    requireNonNull(name, "name");
    requireNonNull(value, "value");

    Vehicle vehicle = orderService.fetchObject(Vehicle.class, name);
    if (vehicle == null) {
      throw new ObjectUnknownException("Unknown vehicle: " + name);
    }

    boolean paused = Boolean.parseBoolean(value);

    kernelExecutor.submit(
        () -> vehicleService.updateVehiclePaused(vehicle.getReference(), paused)
    );
  }

  public void putVehicleCommAdapterEnabled(String name, String value)
      throws ObjectUnknownException, IllegalArgumentException {
    requireNonNull(name, "name");
    requireNonNull(value, "value");

    Vehicle vehicle = orderService.fetchObject(Vehicle.class, name);
    if (vehicle == null) {
      throw new ObjectUnknownException("Unknown vehicle: " + name);
    }

    if (Boolean.parseBoolean(value)) {
      kernelExecutor.submit(
          () -> vehicleService.enableCommAdapter(vehicle.getReference())
      );
    }
    else {
      kernelExecutor.submit(
          () -> vehicleService.disableCommAdapter(vehicle.getReference())
      );
    }
  }

  public AttachmentInformation getVehicleCommAdapterAttachmentInformation(String name)
      throws ObjectUnknownException {
    requireNonNull(name, "name");

    Vehicle vehicle = orderService.fetchObject(Vehicle.class, name);
    if (vehicle == null) {
      throw new ObjectUnknownException("Unknown vehicle: " + name);
    }

    return vehicleService.fetchAttachmentInformation(vehicle.getReference());
  }

  public void putVehicleCommAdapter(String name, String value)
      throws ObjectUnknownException {
    requireNonNull(name, "name");
    requireNonNull(value, "value");

    Vehicle vehicle = orderService.fetchObject(Vehicle.class, name);
    if (vehicle == null) {
      throw new ObjectUnknownException("Unknown vehicle: " + name);
    }

    VehicleCommAdapterDescription newAdapter
        = vehicleService.fetchAttachmentInformation(vehicle.getReference())
            .getAvailableCommAdapters()
            .stream()
            .filter(description -> description.getClass().getName().equals(value))
            .findAny()
            .orElseThrow(
                () -> new IllegalArgumentException("Unknown vehicle driver class name: " + value)
            );

    try {
      kernelExecutor.submit(
          () -> vehicleService.attachCommAdapter(vehicle.getReference(), newAdapter)
      ).get();
    }
    catch (InterruptedException exc) {
      throw new IllegalStateException("Unexpectedly interrupted");
    }
    catch (ExecutionException exc) {
      if (exc.getCause() instanceof RuntimeException) {
        throw (RuntimeException) exc.getCause();
      }
      throw new KernelRuntimeException(exc.getCause());
    }
  }

  public void putVehicleAllowedOrderTypes(String name,
                                          PutVehicleAllowedOrderTypesTO allowedOrderTypes)
      throws ObjectUnknownException {
    requireNonNull(name, "name");
    requireNonNull(allowedOrderTypes, "allowedOrderTypes");

    Vehicle vehicle = orderService.fetchObject(Vehicle.class, name);
    if (vehicle == null) {
      throw new ObjectUnknownException("Unknown vehicle: " + name);
    }

    try {
      kernelExecutor.submit(
          () -> vehicleService.updateVehicleAllowedOrderTypes(
              vehicle.getReference(), new HashSet<>(allowedOrderTypes.getOrderTypes()))
      ).get();
    }
    catch (InterruptedException exc) {
      throw new IllegalStateException("Unexpectedly interrupted");
    }
    catch (ExecutionException exc) {
      if (exc.getCause() instanceof RuntimeException) {
        throw (RuntimeException) exc.getCause();
      }
      throw new KernelRuntimeException(exc.getCause());
    }
  }

  public List<GetOrderSequenceResponseTO> getOrderSequences(@Nullable String intendedVehicle) {
    if (intendedVehicle != null) {
      Vehicle vehicle = orderService.fetchObject(Vehicle.class, intendedVehicle);
      if (vehicle == null) {
        throw new ObjectUnknownException("Unknown vehicle: " + intendedVehicle);
      }
    }

    return orderService.fetchObjects(OrderSequence.class,
                                     new OrderSequenceFilter(intendedVehicle))
        .stream()
        .map(order -> GetOrderSequenceResponseTO.fromOrderSequence(order))
        .collect(Collectors.toList());
  }

  public GetOrderSequenceResponseTO getOrderSequenceByName(String name)
      throws ObjectUnknownException {
    requireNonNull(name, "name");

    return orderService.fetchObjects(OrderSequence.class,
                                     o -> o.getName().equals(name))
        .stream()
        .map(o -> GetOrderSequenceResponseTO.fromOrderSequence(o))
        .findAny()
        .orElseThrow(() -> new ObjectUnknownException("Unknown transport order: " + name));
  }

  public void putOrderSequenceComplete(String name)
      throws ObjectUnknownException,
             IllegalArgumentException,
             InterruptedException,
             ExecutionException {
    requireNonNull(name, "name");

    OrderSequence orderSequence = orderService.fetchObject(OrderSequence.class, name);
    if (orderSequence == null) {
      throw new ObjectUnknownException("Unknown order sequence: " + name);
    }

    try {
      kernelExecutor.submit(
          () -> orderService.markOrderSequenceComplete(orderSequence.getReference())
      ).get();
    }
    catch (InterruptedException exc) {
      throw new IllegalStateException("Unexpectedly interrupted");
    }
    catch (ExecutionException exc) {
      if (exc.getCause() instanceof RuntimeException) {
        throw (RuntimeException) exc.getCause();
      }
      throw new KernelRuntimeException(exc.getCause());
    }
  }
}
