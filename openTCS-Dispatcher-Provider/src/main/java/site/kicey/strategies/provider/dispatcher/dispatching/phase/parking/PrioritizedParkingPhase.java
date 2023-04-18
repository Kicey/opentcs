/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package site.kicey.strategies.provider.dispatcher.dispatching.phase.parking;

import static java.util.Objects.requireNonNull;

import javax.inject.Inject;
import org.opentcs.components.kernel.Router;
import org.opentcs.components.kernel.services.InternalTransportOrderService;
import org.opentcs.data.model.Vehicle;
import site.kicey.strategies.provider.dispatcher.dispatching.DefaultDispatcherConfiguration;
import site.kicey.strategies.provider.dispatcher.dispatching.TransportOrderUtil;
import site.kicey.strategies.provider.dispatcher.dispatching.selection.candidates.CompositeAssignmentCandidateSelectionFilter;
import site.kicey.strategies.provider.dispatcher.dispatching.selection.vehicles.CompositeParkVehicleSelectionFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Creates parking orders for idle vehicles not already at a parking position considering only
 * prioritized parking positions.
 *
 * @author Martin Grzenia (Fraunhofer IML)
 */
public class PrioritizedParkingPhase
    extends AbstractParkingPhase {

  /**
   * This class's logger.
   */
  private static final Logger LOG = LoggerFactory.getLogger(PrioritizedParkingPhase.class);
  /**
   * A filter for selecting vehicles that may be parked.
   */
  private final CompositeParkVehicleSelectionFilter vehicleSelectionFilter;

  @Inject
  public PrioritizedParkingPhase(
      InternalTransportOrderService orderService,
      PrioritizedParkingPositionSupplier parkingPosSupplier,
      Router router,
      CompositeAssignmentCandidateSelectionFilter assignmentCandidateSelectionFilter,
      TransportOrderUtil transportOrderUtil,
      DefaultDispatcherConfiguration configuration,
      CompositeParkVehicleSelectionFilter vehicleSelectionFilter) {
    super(orderService,
          parkingPosSupplier,
          router,
          assignmentCandidateSelectionFilter,
          transportOrderUtil,
          configuration);
    this.vehicleSelectionFilter = requireNonNull(vehicleSelectionFilter, "vehicleSelectionFilter");
  }

  @Override
  public void run() {
    if (!getConfiguration().parkIdleVehicles()
        || !getConfiguration().considerParkingPositionPriorities()) {
      return;
    }

    LOG.debug("Looking for vehicles to send to prioritized parking positions...");

    getOrderService().fetchObjects(Vehicle.class).stream()
        .filter(vehicle -> vehicleSelectionFilter.apply(vehicle).isEmpty())
        .forEach(vehicle -> createParkingOrder(vehicle));
  }
}
