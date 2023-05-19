package site.kicey.strategies.rpc.base.dispatching;

import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.bootstrap.DubboBootstrap;
import org.slf4j.Logger;
import site.kicey.opentcs.strategies.rpc.api.RpcConstant;
import site.kicey.opentcs.strategies.rpc.api.RpcDispatcher;
import site.kicey.strategies.rpc.module.BaseDubboModule;
import site.kicey.strategies.rpc.dispatching.RpcDispatcherProxy;

public class RpcDispatcherModule extends BaseDubboModule {

  private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(RpcDispatcherModule.class);

  /**
   * Utilize the {@link org.apache.dubbo.config.RegistryConfig} from super class to get a Dubbo service instance.
   */
  @Override
  protected void configure() {
    super.configure();
    if (!enabled()) {
      LOG.info("RpcDispatcherModule disabled by configuration.");
      return;
    }

    ReferenceConfig<RpcDispatcher> dispatcherServiceReferenceConfig = new ReferenceConfig<>();
    dispatcherServiceReferenceConfig.setInterface(RpcDispatcher.class);

    DubboBootstrap.getInstance()
        .application(RpcConstant.APPLICATION_NAME)
        .registry(getRegistryConfig())
        .reference(dispatcherServiceReferenceConfig)
        .start();

    RpcDispatcher rpcDispatcher = dispatcherServiceReferenceConfig.get();

    bind(RpcDispatcher.class).toInstance(rpcDispatcher);
    bindDispatcher(RpcDispatcherProxy.class);
  }
}
