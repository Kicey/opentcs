package site.kicey.strategies.rpc.scheduling;

import org.opentcs.customizations.kernel.KernelInjectionModule;

public class RpcSchedulerModule extends KernelInjectionModule {

  /**
   *
   */
  @Override
  protected void configure() {
   bindScheduler(RpcScheduler.class);
  }
}
