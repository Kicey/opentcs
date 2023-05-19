package site.kicey.strategies.rpc.base.scheduling;

import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.bootstrap.DubboBootstrap;
import org.slf4j.Logger;
import site.kicey.opentcs.strategies.rpc.api.RpcConstant;
import site.kicey.opentcs.strategies.rpc.api.RpcScheduler;
import site.kicey.strategies.rpc.module.BaseDubboModule;
import site.kicey.strategies.rpc.scheduling.RpcSchedulerProxy;

public class RpcSchedulerModule extends BaseDubboModule {

  private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(RpcSchedulerModule.class);

  /**
   *
   */
  @Override
  protected void configure() {
    super.configure();
    if (!enabled()) {
      LOG.info("RpcSchedulerModule disabled by configuration.");
      return;
    }

    ReferenceConfig<RpcScheduler> dispatcherServiceReferenceConfig = new ReferenceConfig<>();
    dispatcherServiceReferenceConfig.setInterface(RpcScheduler.class);

    DubboBootstrap.getInstance()
        .application(RpcConstant.APPLICATION_NAME)
        .registry(getRegistryConfig())
        .reference(dispatcherServiceReferenceConfig)
        .start();

    RpcScheduler rpcScheduler = dispatcherServiceReferenceConfig.get();

    bind(RpcScheduler.class).toInstance(rpcScheduler);
    bindScheduler(RpcSchedulerProxy.class);
  }
}
