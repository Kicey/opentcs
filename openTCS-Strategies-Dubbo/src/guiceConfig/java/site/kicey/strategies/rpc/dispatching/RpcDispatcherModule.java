package site.kicey.strategies.rpc.dispatching;

import org.opentcs.customizations.kernel.KernelInjectionModule;

public class RpcDispatcherModule extends KernelInjectionModule {

  @Override
  protected void configure() {
    bindDispatcher(RpcDispatcher.class);
  }
}
