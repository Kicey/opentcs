package site.kicey.strategies.rpc.module;

import org.apache.dubbo.config.RegistryConfig;
import org.opentcs.customizations.kernel.KernelInjectionModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.kicey.strategies.rpc.DubboConfiguration;

public class BaseDubboModule extends KernelInjectionModule {

  private static final Logger LOG = LoggerFactory.getLogger(BaseDubboModule.class);

  private DubboConfiguration dubboConfiguration;

  private RegistryConfig registryConfig;

  /**
   * {@inheritDoc}
   */
  @Override
  protected void configure() {
    super.configure();
    dubboConfiguration
        = getConfigBindingProvider().get(DubboConfiguration.PREFIX, DubboConfiguration.class);

    if (!enabled()) {
      LOG.info("Dubbo module disabled by configuration.");
      return;
    }
  }

  protected DubboConfiguration getDubboConfiguration() {
    return dubboConfiguration;
  }

  protected Boolean enabled(){
    return getDubboConfiguration().enable();
  }

  protected RegistryConfig getRegistryConfig() {
    return new RegistryConfig(getDubboConfiguration().zookeeperAddress());
  }
}
