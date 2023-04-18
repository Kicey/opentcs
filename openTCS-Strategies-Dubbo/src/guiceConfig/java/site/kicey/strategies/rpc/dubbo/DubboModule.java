package site.kicey.strategies.rpc.dubbo;

import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.bootstrap.DubboBootstrap;
import org.opentcs.customizations.kernel.KernelInjectionModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.kicey.opentcs.strategies.rpc.api.RpcConstant;
import site.kicey.opentcs.strategies.rpc.api.RpcDispatcherService;
import site.kicey.opentcs.strategies.rpc.api.RpcRouterService;
import site.kicey.opentcs.strategies.rpc.api.RpcSchedulerService;
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
    ReferenceConfig<RpcDispatcherService> dispatcherServiceReferenceConfig = new ReferenceConfig<>();
    dispatcherServiceReferenceConfig.setInterface(RpcDispatcherService.class);
    // Router service reference
    ReferenceConfig<RpcRouterService> routerServiceReferenceConfig = new ReferenceConfig<>();
    routerServiceReferenceConfig.setInterface(RpcRouterService.class);
    // Scheduler service reference
    ReferenceConfig<RpcSchedulerService> schedulerServiceReferenceConfig = new ReferenceConfig<>();
    schedulerServiceReferenceConfig.setInterface(RpcSchedulerService.class);

    RegistryConfig registryConfig = new RegistryConfig(configuration.zookeeperAddress());

    DubboBootstrap.getInstance()
        .application(RpcConstant.APPLICATION_NAME)
        .registry(registryConfig)
        .reference(dispatcherServiceReferenceConfig)
        .reference(routerServiceReferenceConfig)
        .reference(schedulerServiceReferenceConfig)
        .start();

    RpcDispatcherService rpcDispatcherService = dispatcherServiceReferenceConfig.get();
    RpcRouterService rpcRouterService = routerServiceReferenceConfig.get();
    RpcSchedulerService rpcSchedulerService = schedulerServiceReferenceConfig.get();

    bind(RpcDispatcherService.class).toInstance(rpcDispatcherService);
    bind(RpcRouterService.class).toInstance(rpcRouterService);
    bind(RpcSchedulerService.class).toInstance(rpcSchedulerService);
  }
}
