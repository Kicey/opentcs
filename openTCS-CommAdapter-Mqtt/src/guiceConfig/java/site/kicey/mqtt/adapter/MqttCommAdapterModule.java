package site.kicey.mqtt.adapter;

import com.google.inject.assistedinject.FactoryModuleBuilder;
import org.opentcs.customizations.kernel.KernelInjectionModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.kicey.MqttAdapterComponentsFactory;
import site.kicey.MqttAdapterConfiguration;
import site.kicey.MqttCommunicationAdapterFactory;

public class MqttCommAdapterModule extends KernelInjectionModule {

  private static final Logger LOG = LoggerFactory.getLogger(MqttCommAdapterModule.class);

  /**
   * {@inheritDoc
   */
  @Override
  protected void configure() {
    LOG.info("????????????????");
    MqttAdapterConfiguration configuration
        = getConfigBindingProvider().get(MqttAdapterConfiguration.PREFIX,
                                         MqttAdapterConfiguration.class);

    if (!configuration.enable()) {
      LOG.info("Mqtt communication adapter disabled by configuration.");
      return;
    }

    install(new FactoryModuleBuilder().build(MqttAdapterComponentsFactory.class));
    vehicleCommAdaptersBinder().addBinding().to(MqttCommunicationAdapterFactory.class);
  }
}
