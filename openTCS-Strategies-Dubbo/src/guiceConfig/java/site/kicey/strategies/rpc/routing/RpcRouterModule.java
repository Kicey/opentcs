package site.kicey.strategies.rpc.routing;

import org.opentcs.customizations.kernel.KernelInjectionModule;

public class RpcRouterModule extends KernelInjectionModule {

  @Override
  protected void configure() {
    bindRouter(RpcRouter.class);
  }
}
