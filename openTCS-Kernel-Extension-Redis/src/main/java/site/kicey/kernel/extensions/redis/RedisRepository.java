package site.kicey.kernel.extensions.redis;

import com.google.inject.Inject;
import org.opentcs.components.kernel.KernelExtension;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class RedisRepository implements KernelExtension {

  private final RedisStoreConfiguration redisStoreConfiguration;

  @Inject
  public RedisRepository(RedisStoreConfiguration redisStoreConfiguration){
    this.redisStoreConfiguration = redisStoreConfiguration;
  }

  @Override
  public void initialize() {
    Config config = new Config();
    config.useSingleServer().setAddress(redisStoreConfiguration.address());

    RedissonClient redissonClient = Redisson.create(config);
  }

  @Override
  public boolean isInitialized() {
    return false;
  }

  @Override
  public void terminate() {

  }
}
