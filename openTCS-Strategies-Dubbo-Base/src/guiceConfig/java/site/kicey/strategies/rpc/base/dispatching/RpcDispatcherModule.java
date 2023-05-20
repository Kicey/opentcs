package site.kicey.strategies.rpc.base.dispatching;

import org.slf4j.Logger;
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

    bindDispatcher(RpcDispatcherProxy.class);
  }
}
