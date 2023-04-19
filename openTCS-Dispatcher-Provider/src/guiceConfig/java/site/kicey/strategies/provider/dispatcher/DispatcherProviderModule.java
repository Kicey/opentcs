package site.kicey.strategies.provider.dispatcher;

import org.opentcs.customizations.kernel.KernelInjectionModule;
import site.kicey.opentcs.strategies.rpc.proxy.RouterRpcProxy;
import site.kicey.opentcs.strategies.rpc.proxy.SchedulerRpcProxy;
import site.kicey.strategies.provider.dispatcher.dispatching.DefaultDispatcher;

public class DispatcherProviderModule extends KernelInjectionModule {

  @Override
  protected void configure() {
    bindDispatcher(DefaultDispatcher.class);
    bindScheduler(SchedulerRpcProxy.class);
    bindRouter(RouterRpcProxy.class);
  }
}
