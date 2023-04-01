package site.kicey.kernel.extensions.redis;

import org.opentcs.configuration.ConfigurationEntry;
import org.opentcs.configuration.ConfigurationPrefix;

@ConfigurationPrefix(RedisStoreConfiguration.PREFIX)
public interface RedisStoreConfiguration {
  String PREFIX = "redisrepository";

  @ConfigurationEntry(
      type = "Boolean",
      description = "Whether to enable the interface.",
      orderKey = "0")
  boolean enable();

  @ConfigurationEntry(
      type = "String",
      description = "The address of the redis broker.")
  String address();

  @ConfigurationEntry(
      type = "String",
      description = "The password of the redis broker.")
  String password();
}
