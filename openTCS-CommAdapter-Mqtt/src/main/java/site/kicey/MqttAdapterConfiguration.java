package site.kicey;

import org.opentcs.configuration.ConfigurationEntry;
import org.opentcs.configuration.ConfigurationPrefix;

@ConfigurationPrefix(MqttAdapterConfiguration.PREFIX)
public interface MqttAdapterConfiguration {
  String PREFIX = "mqtt-commadapter";

  @ConfigurationEntry(
      type = "Boolean",
      description = "Whether to enable to register/enable the mqtt driver.",
      orderKey = "0_enable")
  boolean enable();
}
