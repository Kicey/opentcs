package site.kicey.strategies.rpc.dubbo;

import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.bootstrap.DubboBootstrap;
import org.opentcs.customizations.kernel.KernelInjectionModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.kicey.opentcs.strategies.rpc.api.RpcConstant;
import site.kicey.opentcs.strategies.rpc.api.RpcDispatcher;
import site.kicey.opentcs.strategies.rpc.api.RpcRouter;
import site.kicey.opentcs.strategies.rpc.api.RpcScheduler;
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
    RpcRouter rpcRouter = routerServiceReferenceConfig.get();
    RpcScheduler rpcScheduler = schedulerServiceReferenceConfig.get();

    bind(RpcDispatcher.class).toInstance(rpcDispatcher);
    bind(RpcRouter.class).toInstance(rpcRouter);
    bind(RpcScheduler.class).toInstance(rpcScheduler);
  }
}
