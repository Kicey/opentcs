package site.kicey.strategies.rpc.base.scheduling;

import org.slf4j.Logger;
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

    bindScheduler(RpcSchedulerProxy.class);
  }
}
