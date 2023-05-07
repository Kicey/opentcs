package site.kicey.strategies.provider.peripheraljobdispatcher;

import org.opentcs.customizations.kernel.KernelInjectionModule;
import org.opentcs.drivers.peripherals.PeripheralJobCallback;
import site.kicey.opentcs.strategies.rpc.proxy.DispatcherRpcProxy;
import site.kicey.opentcs.strategies.rpc.proxy.RouterRpcProxy;
import site.kicey.opentcs.strategies.rpc.proxy.SchedulerRpcProxy;
import site.kicey.strategies.provider.peripheraljobdispatcher.peripherals.dispatching.DefaultJobSelectionStrategy;
import site.kicey.strategies.provider.peripheraljobdispatcher.peripherals.dispatching.DefaultPeripheralJobDispatcher;
import site.kicey.strategies.provider.peripheraljobdispatcher.peripherals.dispatching.DefaultPeripheralJobDispatcherConfiguration;
import site.kicey.strategies.provider.peripheraljobdispatcher.peripherals.dispatching.DefaultPeripheralReleaseStrategy;
import site.kicey.strategies.provider.peripheraljobdispatcher.peripherals.dispatching.JobSelectionStrategy;
import site.kicey.strategies.provider.peripheraljobdispatcher.peripherals.dispatching.PeripheralReleaseStrategy;

public class PeripheralJobDispatcherProviderModule extends KernelInjectionModule {
  /**
   * Creates a new instance.
   */
  public PeripheralJobDispatcherProviderModule() {
  }

  @Override
  protected void configure() {
    configureDispatcherDependencies();
    bindPeripheralJobDispatcher(DefaultPeripheralJobDispatcher.class);
    bindDispatcher(DispatcherRpcProxy.class);
    bindScheduler(SchedulerRpcProxy.class);
    bindRouter(RouterRpcProxy.class);
  }

  private void configureDispatcherDependencies() {
    bind(DefaultPeripheralJobDispatcherConfiguration.class)
        .toInstance(
            getConfigBindingProvider().get(DefaultPeripheralJobDispatcherConfiguration.PREFIX,
                DefaultPeripheralJobDispatcherConfiguration.class)
        );

    bind(PeripheralJobCallback.class).to(DefaultPeripheralJobDispatcher.class);
    bind(PeripheralReleaseStrategy.class).to(DefaultPeripheralReleaseStrategy.class);
    bind(JobSelectionStrategy.class).to(DefaultJobSelectionStrategy.class);
  }
}
