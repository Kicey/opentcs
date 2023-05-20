package site.kicey.strategies.rpc.base.peripherals.dispatching;

import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.bootstrap.DubboBootstrap;
import org.slf4j.Logger;
import site.kicey.opentcs.strategies.rpc.api.RpcConstant;
import site.kicey.opentcs.strategies.rpc.api.RpcPeripheralJobDispatcher;
import site.kicey.strategies.rpc.module.BaseDubboModule;
import site.kicey.strategies.rpc.peripherals.dispatching.RpcPeripheralJobDispatcherProxy;

public class RpcPeripheralJobDispatcherModule extends BaseDubboModule {

  private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(RpcPeripheralJobDispatcherModule.class);

  @Override
  protected void configure() {
    super.configure();
    if (!enabled()) {
      LOG.info("RpcPeripheralJobDispatcherModule disabled by configuration.");
      return;
    }

    bindPeripheralJobDispatcher(RpcPeripheralJobDispatcherProxy.class);
  }
}
