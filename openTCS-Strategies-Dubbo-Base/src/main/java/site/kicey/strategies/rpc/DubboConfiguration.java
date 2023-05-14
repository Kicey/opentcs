package site.kicey.strategies.rpc;

import org.opentcs.configuration.ConfigurationEntry;
import org.opentcs.configuration.ConfigurationPrefix;

@ConfigurationPrefix(DubboConfiguration.PREFIX)
public interface DubboConfiguration {
  String PREFIX = "dubbo";
  @ConfigurationEntry(
      type = "Boolean",
      description = "Whether to enable the interface.",
      orderKey = "0")
  boolean enable();

  @ConfigurationEntry(
      type = "String",
      description = "The address of zookeeper server.",
      orderKey = "0")
  String zookeeperAddress();
}
