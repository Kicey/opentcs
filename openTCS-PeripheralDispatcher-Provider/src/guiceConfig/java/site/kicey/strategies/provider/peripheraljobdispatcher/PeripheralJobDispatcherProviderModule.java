package site.kicey.strategies.provider.peripheraljobdispatcher;

import org.opentcs.customizations.kernel.KernelInjectionModule;
import org.opentcs.drivers.peripherals.PeripheralJobCallback;
import site.kicey.strategies.provider.peripheraljobdispatcher.peripherals.dispatching.DefaultJobSelectionStrategy;
import site.kicey.strategies.provider.peripheraljobdispatcher.peripherals.dispatching.DefaultPeripheralJobDispatcher;
import site.kicey.strategies.provider.peripheraljobdispatcher.peripherals.dispatching.DefaultPeripheralJobDispatcherConfiguration;
import site.kicey.strategies.provider.peripheraljobdispatcher.peripherals.dispatching.DefaultPeripheralReleaseStrategy;
import site.kicey.strategies.provider.peripheraljobdispatcher.peripherals.dispatching.JobSelectionStrategy;
import site.kicey.strategies.provider.peripheraljobdispatcher.peripherals.dispatching.PeripheralReleaseStrategy;
import site.kicey.strategies.rpc.dispatching.RpcDispatcherProxy;
import site.kicey.strategies.rpc.routing.RpcRouterProxy;
import site.kicey.strategies.rpc.scheduling.RpcSchedulerProxy;

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
    bindDispatcher(RpcDispatcherProxy.class);
    bindScheduler(RpcSchedulerProxy.class);
    bindRouter(RpcRouterProxy.class);
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
