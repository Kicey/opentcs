package site.kicey.strategies.rpc.base.routing;

import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.bootstrap.DubboBootstrap;
import org.slf4j.Logger;
import site.kicey.opentcs.strategies.rpc.api.RpcConstant;
import site.kicey.opentcs.strategies.rpc.api.RpcRouter;
import site.kicey.strategies.rpc.base.dubbo.BaseDubboModule;
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

    ReferenceConfig<RpcRouter> dispatcherServiceReferenceConfig = new ReferenceConfig<>();
    dispatcherServiceReferenceConfig.setInterface(RpcRouter.class);

    DubboBootstrap.getInstance()
        .application(RpcConstant.APPLICATION_NAME)
        .registry(getRegistryConfig())
        .reference(dispatcherServiceReferenceConfig)
        .start();

    RpcRouter rpcRouter = dispatcherServiceReferenceConfig.get();

    bind(RpcRouter.class).toInstance(rpcRouter);
    bindRouter(RpcRouterProxy.class);
  }
}
