package site.kicey.strategies.rpc.dubbo;

import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.bootstrap.DubboBootstrap;
import org.opentcs.customizations.kernel.KernelInjectionModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.kicey.opentcs.strategies.rpc.api.*;
import site.kicey.strategies.rpc.DubboConfiguration;

public class DubboModule extends KernelInjectionModule {

  private static final Logger LOG = LoggerFactory.getLogger(DubboModule.class);

  /**
   * {@inheritDoc}
   */
  @Override
  protected void configure() {
    super.configure();
    DubboConfiguration configuration
        = getConfigBindingProvider().get(DubboConfiguration.PREFIX, DubboConfiguration.class);

    if (!configuration.enable()) {
      LOG.info("Dubbo module disabled by configuration.");
      return;
    }

    // Dispatcher service reference
    ReferenceConfig<RpcDispatcher> dispatcherServiceReferenceConfig = new ReferenceConfig<>();
    dispatcherServiceReferenceConfig.setInterface(RpcDispatcher.class);
    // Peripheral job dispatcher service reference
    ReferenceConfig<RpcPeripheralJobDispatcher> peripheralJobDispatcherReferenceConfig = new ReferenceConfig<>();
    peripheralJobDispatcherReferenceConfig.setInterface(RpcPeripheralJobDispatcher.class);
    // Router service reference
    ReferenceConfig<RpcRouter> routerServiceReferenceConfig = new ReferenceConfig<>();
    routerServiceReferenceConfig.setInterface(RpcRouter.class);
    // Scheduler service reference
    ReferenceConfig<RpcScheduler> schedulerServiceReferenceConfig = new ReferenceConfig<>();
    schedulerServiceReferenceConfig.setInterface(RpcScheduler.class);

    RegistryConfig registryConfig = new RegistryConfig(configuration.zookeeperAddress());

    DubboBootstrap.getInstance()
        .application(RpcConstant.APPLICATION_NAME)
        .registry(registryConfig)
        .reference(dispatcherServiceReferenceConfig)
        .reference(routerServiceReferenceConfig)
        .reference(schedulerServiceReferenceConfig)
        .start();

    RpcDispatcher rpcDispatcher = dispatcherServiceReferenceConfig.get();
    RpcPeripheralJobDispatcher rpcPeripheralJobDispatcher = peripheralJobDispatcherReferenceConfig.get();
    RpcRouter rpcRouter = routerServiceReferenceConfig.get();
    RpcScheduler rpcScheduler = schedulerServiceReferenceConfig.get();

    bind(RpcDispatcher.class).toInstance(rpcDispatcher);
    bind(RpcRouter.class).toInstance(rpcRouter);
    bind(RpcScheduler.class).toInstance(rpcScheduler);
  }
}
