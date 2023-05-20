package site.kicey.strategies.rpc.base.routing;

import org.slf4j.Logger;
import site.kicey.strategies.rpc.module.BaseDubboModule;
import site.kicey.strategies.rpc.routing.RpcRouterProxy;

public class RpcRouterModule extends BaseDubboModule {

  private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(RpcRouterModule.class);

  @Override
  protected void configure() {
    super.configure();
    if (!enabled()) {
      LOG.info("RpcRouterModule disabled by configuration.");
      return;
    }

    bindRouter(RpcRouterProxy.class);
  }
}
