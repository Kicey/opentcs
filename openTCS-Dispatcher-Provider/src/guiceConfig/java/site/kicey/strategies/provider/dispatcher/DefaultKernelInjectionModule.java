/**
 * Copyright (c) The openTCS Authors.
 * <p>
 * This program is free software and subject to the MIT license. (For details, see the licensing
 * information (LICENSE.txt) you should have received with this copy of the software.)
 */
package site.kicey.strategies.provider.dispatcher;

import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.multibindings.MapBinder;
import com.google.inject.multibindings.OptionalBinder;
import java.io.File;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import javax.inject.Singleton;
import org.opentcs.kernelbase.KernelApplicationConfiguration;
import org.opentcs.kernelbase.KernelState;
import org.opentcs.kernelbase.KernelStateModelling;
import org.opentcs.kernelbase.KernelStateOperating;
import org.opentcs.kernelbase.KernelStateShutdown;
import org.opentcs.kernelbase.OrderPoolConfiguration;
import org.opentcs.kernelbase.SslConfiguration;
import org.opentcs.kernelbase.StandardKernel;
import org.opentcs.access.Kernel;
import org.opentcs.access.LocalKernel;
import org.opentcs.access.SslParameterSet;
import org.opentcs.common.LoggingScheduledThreadPoolExecutor;
import org.opentcs.components.kernel.ObjectNameProvider;
import org.opentcs.components.kernel.services.DispatcherService;
import org.opentcs.components.kernel.services.InternalPeripheralJobService;
import org.opentcs.components.kernel.services.InternalPeripheralService;
import org.opentcs.components.kernel.services.InternalPlantModelService;
import org.opentcs.components.kernel.services.InternalQueryService;
import org.opentcs.components.kernel.services.InternalTransportOrderService;
import org.opentcs.components.kernel.services.InternalVehicleService;
import org.opentcs.components.kernel.services.NotificationService;
import org.opentcs.components.kernel.services.PeripheralDispatcherService;
import org.opentcs.components.kernel.services.PeripheralJobService;
import org.opentcs.components.kernel.services.PeripheralService;
import org.opentcs.components.kernel.services.PlantModelService;
import org.opentcs.components.kernel.services.QueryService;
import org.opentcs.components.kernel.services.RouterService;
import org.opentcs.components.kernel.services.SchedulerService;
import org.opentcs.components.kernel.services.TCSObjectService;
import org.opentcs.components.kernel.services.TransportOrderService;
import org.opentcs.components.kernel.services.VehicleService;
import org.opentcs.customizations.ApplicationEventBus;
import org.opentcs.customizations.ApplicationHome;
import org.opentcs.customizations.kernel.GlobalSyncObject;
import org.opentcs.customizations.kernel.KernelExecutor;
import org.opentcs.customizations.kernel.KernelInjectionModule;
import org.opentcs.drivers.peripherals.PeripheralControllerPool;
import org.opentcs.drivers.vehicle.VehicleControllerPool;
import org.opentcs.kernelbase.extensions.controlcenter.vehicles.AttachmentManager;
import org.opentcs.kernelbase.extensions.controlcenter.vehicles.VehicleEntryPool;
import org.opentcs.kernelbase.peripherals.DefaultPeripheralControllerPool;
import org.opentcs.kernelbase.peripherals.LocalPeripheralControllerPool;
import org.opentcs.kernelbase.peripherals.PeripheralAttachmentManager;
import org.opentcs.kernelbase.peripherals.PeripheralCommAdapterRegistry;
import org.opentcs.kernelbase.peripherals.PeripheralControllerFactory;
import org.opentcs.kernelbase.peripherals.PeripheralEntryPool;
import org.opentcs.kernelbase.persistence.ModelPersister;
import org.opentcs.kernelbase.persistence.XMLFileModelPersister;
import org.opentcs.kernelbase.services.StandardDispatcherService;
import org.opentcs.kernelbase.services.StandardNotificationService;
import org.opentcs.kernelbase.services.StandardPeripheralDispatcherService;
import org.opentcs.kernelbase.services.StandardPeripheralJobService;
import org.opentcs.kernelbase.services.StandardPeripheralService;
import org.opentcs.kernelbase.services.StandardPlantModelService;
import org.opentcs.kernelbase.services.StandardQueryService;
import org.opentcs.kernelbase.services.StandardRouterService;
import org.opentcs.kernelbase.services.StandardSchedulerService;
import org.opentcs.kernelbase.services.StandardTCSObjectService;
import org.opentcs.kernelbase.services.StandardTransportOrderService;
import org.opentcs.kernelbase.services.StandardVehicleService;
import org.opentcs.kernelbase.vehicles.DefaultVehicleControllerPool;
import org.opentcs.kernelbase.vehicles.LocalVehicleControllerPool;
import org.opentcs.kernelbase.vehicles.VehicleCommAdapterRegistry;
import org.opentcs.kernelbase.vehicles.VehicleControllerComponentsFactory;
import org.opentcs.kernelbase.vehicles.VehicleControllerFactory;
import org.opentcs.kernelbase.workingset.InMemoryTCSObjectRepository;
import org.opentcs.kernelbase.workingset.NotificationBuffer;
import org.opentcs.kernelbase.workingset.PeripheralJobPoolManager;
import org.opentcs.kernelbase.workingset.PlantModelManager;
import org.opentcs.kernelbase.workingset.PrefixedUlidObjectNameProvider;
import org.opentcs.kernelbase.workingset.TCSObjectManager;
import org.opentcs.kernelbase.workingset.TransportOrderPoolManager;
import org.opentcs.util.event.EventBus;
import org.opentcs.util.event.EventHandler;
import org.opentcs.util.event.SimpleEventBus;
import org.opentcs.util.logging.UncaughtExceptionLogger;
import org.opentcs.workingset.TCSObjectRepository;

/**
 * A Guice module for the openTCS kernel application.
 *
 * @author Stefan Walter (Fraunhofer IML)
 */
public class DefaultKernelInjectionModule
    extends KernelInjectionModule {

  /**
   * Creates a new instance.
   */
  public DefaultKernelInjectionModule() {
  }

  @Override
  protected void configure() {
    configureEventHub();
    configureKernelExecutor();

    // Ensure that the application's home directory can be used everywhere.
    File applicationHome = new File(System.getProperty("opentcs.home", "."));
    bind(File.class)
        .annotatedWith(ApplicationHome.class)
        .toInstance(applicationHome);

    // A single global synchronization object for the kernel.
    bind(Object.class)
        .annotatedWith(GlobalSyncObject.class)
        .to(Object.class)
        .in(Singleton.class);

    // The kernel's data pool structures.
    OptionalBinder.newOptionalBinder(binder(), TCSObjectRepository.class)
        .setDefault()
        .to(InMemoryTCSObjectRepository.class).in(Singleton.class);
    bind(TCSObjectManager.class).in(Singleton.class);
    bind(PlantModelManager.class).in(Singleton.class);
    bind(TransportOrderPoolManager.class).in(Singleton.class);
    bind(PeripheralJobPoolManager.class).in(Singleton.class);
    bind(NotificationBuffer.class).in(Singleton.class);

    bind(ObjectNameProvider.class)
        .to(PrefixedUlidObjectNameProvider.class)
        .in(Singleton.class);

    configurePersistence();

    bind(VehicleCommAdapterRegistry.class)
        .in(Singleton.class);

    configureVehicleControllers();

    bind(AttachmentManager.class)
        .in(Singleton.class);
    bind(VehicleEntryPool.class)
        .in(Singleton.class);

    configurePeripheralControllers();

    bind(PeripheralCommAdapterRegistry.class)
        .in(Singleton.class);
    bind(PeripheralAttachmentManager.class)
        .in(Singleton.class);
    bind(PeripheralEntryPool.class)
        .in(Singleton.class);

    bind(StandardKernel.class)
        .in(Singleton.class);
    bind(LocalKernel.class)
        .to(StandardKernel.class);

    configureKernelStatesDependencies();
    configureKernelStarterDependencies();
    configureSslParameters();
    configureKernelServicesDependencies();

    // Ensure all of these binders are initialized.
    extensionsBinderAllModes();
    extensionsBinderModelling();
    extensionsBinderOperating();
    vehicleCommAdaptersBinder();
    peripheralCommAdaptersBinder();
  }

  private void configureKernelServicesDependencies() {
    bind(StandardPlantModelService.class).in(Singleton.class);
    bind(PlantModelService.class).to(StandardPlantModelService.class);
    bind(InternalPlantModelService.class).to(StandardPlantModelService.class);

    bind(StandardTransportOrderService.class).in(Singleton.class);
    bind(TransportOrderService.class).to(StandardTransportOrderService.class);
    bind(InternalTransportOrderService.class).to(StandardTransportOrderService.class);

    bind(StandardVehicleService.class).in(Singleton.class);
    bind(VehicleService.class).to(StandardVehicleService.class);
    bind(InternalVehicleService.class).to(StandardVehicleService.class);

    bind(StandardTCSObjectService.class).in(Singleton.class);
    bind(TCSObjectService.class).to(StandardTCSObjectService.class);

    bind(StandardNotificationService.class).in(Singleton.class);
    bind(NotificationService.class).to(StandardNotificationService.class);

    bind(StandardRouterService.class).in(Singleton.class);
    bind(RouterService.class).to(StandardRouterService.class);

    bind(StandardDispatcherService.class).in(Singleton.class);
    bind(DispatcherService.class).to(StandardDispatcherService.class);

    bind(StandardSchedulerService.class).in(Singleton.class);
    bind(SchedulerService.class).to(StandardSchedulerService.class);

    bind(StandardQueryService.class).in(Singleton.class);
    bind(QueryService.class).to(StandardQueryService.class);
    bind(InternalQueryService.class).to(StandardQueryService.class);

    bind(StandardPeripheralService.class).in(Singleton.class);
    bind(PeripheralService.class).to(StandardPeripheralService.class);
    bind(InternalPeripheralService.class).to(StandardPeripheralService.class);

    bind(StandardPeripheralJobService.class).in(Singleton.class);
    bind(PeripheralJobService.class).to(StandardPeripheralJobService.class);
    bind(InternalPeripheralJobService.class).to(StandardPeripheralJobService.class);

    bind(StandardPeripheralDispatcherService.class).in(Singleton.class);
    bind(PeripheralDispatcherService.class).to(StandardPeripheralDispatcherService.class);
  }

  private void configureVehicleControllers() {
    install(new FactoryModuleBuilder().build(VehicleControllerFactory.class));
    install(new FactoryModuleBuilder().build(VehicleControllerComponentsFactory.class));

    bind(DefaultVehicleControllerPool.class)
        .in(Singleton.class);
    bind(VehicleControllerPool.class)
        .to(DefaultVehicleControllerPool.class);
    bind(LocalVehicleControllerPool.class)
        .to(DefaultVehicleControllerPool.class);
  }

  private void configurePeripheralControllers() {
    install(new FactoryModuleBuilder().build(PeripheralControllerFactory.class));

    bind(DefaultPeripheralControllerPool.class)
        .in(Singleton.class);
    bind(PeripheralControllerPool.class)
        .to(DefaultPeripheralControllerPool.class);
    bind(LocalPeripheralControllerPool.class)
        .to(DefaultPeripheralControllerPool.class);
  }

  private void configurePersistence() {
    bind(ModelPersister.class).to(XMLFileModelPersister.class);
  }

  private void configureEventHub() {
    EventBus newEventBus = new SimpleEventBus();
    bind(EventHandler.class)
        .annotatedWith(ApplicationEventBus.class)
        .toInstance(newEventBus);
    bind(org.opentcs.util.event.EventSource.class)
        .annotatedWith(ApplicationEventBus.class)
        .toInstance(newEventBus);
    bind(EventBus.class)
        .annotatedWith(ApplicationEventBus.class)
        .toInstance(newEventBus);
  }

  private void configureKernelStatesDependencies() {
    // A map for KernelState instances to be provided at runtime.
    MapBinder<Kernel.State, KernelState> stateMapBinder
        = MapBinder.newMapBinder(binder(), Kernel.State.class, KernelState.class);
    stateMapBinder.addBinding(Kernel.State.SHUTDOWN).to(KernelStateShutdown.class);
    stateMapBinder.addBinding(Kernel.State.MODELLING).to(KernelStateModelling.class);
    stateMapBinder.addBinding(Kernel.State.OPERATING).to(KernelStateOperating.class);

    bind(OrderPoolConfiguration.class)
        .toInstance(getConfigBindingProvider().get(OrderPoolConfiguration.PREFIX,
            OrderPoolConfiguration.class));

    transportOrderCleanupApprovalBinder();
    orderSequenceCleanupApprovalBinder();
    peripheralJobCleanupApprovalBinder();
  }

  private void configureKernelStarterDependencies() {
    bind(KernelApplicationConfiguration.class)
        .toInstance(getConfigBindingProvider().get(KernelApplicationConfiguration.PREFIX,
            KernelApplicationConfiguration.class));
  }

  private void configureSslParameters() {
    SslConfiguration configuration
        = getConfigBindingProvider().get(SslConfiguration.PREFIX,
        SslConfiguration.class);
    SslParameterSet sslParamSet = new SslParameterSet(SslParameterSet.DEFAULT_KEYSTORE_TYPE,
        new File(configuration.keystoreFile()),
        configuration.keystorePassword(),
        new File(configuration.truststoreFile()),
        configuration.truststorePassword());
    bind(SslParameterSet.class).toInstance(sslParamSet);
  }

  private void configureKernelExecutor() {
    ScheduledExecutorService executor
        = new LoggingScheduledThreadPoolExecutor(
        1,
        runnable -> {
          Thread thread = new Thread(runnable, "kernelExecutor");
          thread.setUncaughtExceptionHandler(new UncaughtExceptionLogger(false));
          return thread;
        }
    );
    bind(ScheduledExecutorService.class)
        .annotatedWith(KernelExecutor.class)
        .toInstance(executor);
    bind(ExecutorService.class)
        .annotatedWith(KernelExecutor.class)
        .toInstance(executor);
    bind(Executor.class)
        .annotatedWith(KernelExecutor.class)
        .toInstance(executor);
  }
}
