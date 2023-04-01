package site.kicey.kernel.extensions.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.google.inject.Singleton;
import com.google.inject.multibindings.OptionalBinder;
import org.opentcs.customizations.kernel.KernelInjectionModule;
import org.opentcs.data.TCSObjectReference;
import org.opentcs.data.model.Block;
import org.opentcs.data.model.Block.Layout;
import org.opentcs.data.model.Couple;
import org.opentcs.data.model.Group;
import org.opentcs.data.model.Location;
import org.opentcs.data.model.Location.Link;
import org.opentcs.data.model.LocationType;
import org.opentcs.data.model.Path;
import org.opentcs.data.model.PeripheralInformation;
import org.opentcs.data.model.PlantModel;
import org.opentcs.data.model.Point;
import org.opentcs.data.model.TCSResource;
import org.opentcs.data.model.TCSResourceReference;
import org.opentcs.data.model.Triple;
import org.opentcs.data.model.Vehicle;
import org.opentcs.data.model.visualization.ImageData;
import org.opentcs.data.model.visualization.ImageLayoutElement;
import org.opentcs.data.model.visualization.Layer;
import org.opentcs.data.model.visualization.LayerGroup;
import org.opentcs.data.model.visualization.ModelLayoutElement;
import org.opentcs.data.model.visualization.ShapeLayoutElement;
import org.opentcs.data.model.visualization.VisualLayout;
import org.opentcs.workingset.TCSObjectRepository;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.kicey.kernel.extensions.redis.mixin.TCSObjectReferenceMixIn;
import site.kicey.kernel.extensions.redis.mixin.model.BlockMixIn;
import site.kicey.kernel.extensions.redis.mixin.model.BlockMixIn.LayoutMixIn;
import site.kicey.kernel.extensions.redis.mixin.model.CoupleMixIn;
import site.kicey.kernel.extensions.redis.mixin.model.GroupMixIn;
import site.kicey.kernel.extensions.redis.mixin.model.LocationMixIn;
import site.kicey.kernel.extensions.redis.mixin.model.LocationMixIn.LinkMixIn;
import site.kicey.kernel.extensions.redis.mixin.model.LocationTypeMixIn;
import site.kicey.kernel.extensions.redis.mixin.model.PathMixIn;
import site.kicey.kernel.extensions.redis.mixin.model.PeripheralInformationMixIn;
import site.kicey.kernel.extensions.redis.mixin.model.PlantModelMixIn;
import site.kicey.kernel.extensions.redis.mixin.model.PointMixIn;
import site.kicey.kernel.extensions.redis.mixin.model.TCSResourceMixIn;
import site.kicey.kernel.extensions.redis.mixin.model.TCSResourceReferenceMixIn;
import site.kicey.kernel.extensions.redis.mixin.model.TripleMixIn;
import site.kicey.kernel.extensions.redis.mixin.model.VehicleMixIn;
import site.kicey.kernel.extensions.redis.mixin.model.visualization.ImageDataMixIn;
import site.kicey.kernel.extensions.redis.mixin.model.visualization.ImageLayoutElementMixIn;
import site.kicey.kernel.extensions.redis.mixin.model.visualization.LayerGroupMixIn;
import site.kicey.kernel.extensions.redis.mixin.model.visualization.LayerMixIn;
import site.kicey.kernel.extensions.redis.mixin.model.visualization.ModelLayoutElementMixIn;
import site.kicey.kernel.extensions.redis.mixin.model.visualization.ShapeLayoutElementMixIn;
import site.kicey.kernel.extensions.redis.mixin.model.visualization.VisualLayoutMixIn;
import site.kicey.kernel.extensions.redis.workingset.RedisTCSObjectRepository;


public class RedisStoreModule extends KernelInjectionModule {

  private static final Logger LOG = LoggerFactory.getLogger(RedisStoreModule.class);

  @Override
  protected void configure() {
    RedisStoreConfiguration configuration
        = getConfigBindingProvider().get(RedisStoreConfiguration.PREFIX,
                                         RedisStoreConfiguration.class);
    if (!configuration.enable()) {
      LOG.info("RedisStore disabled by configuration.");
      return;
    }

    RedissonClient redisClient = initializeRedisClient(configuration);

    bind(RedisStoreConfiguration.class)
        .toInstance(configuration);

    bind(RedissonClient.class)
        .toInstance(redisClient);

    OptionalBinder.newOptionalBinder(binder(), TCSObjectRepository.class)
        .setBinding()
        .to(RedisTCSObjectRepository.class)
        .in(Singleton.class);
    extensionsBinderOperating().addBinding()
        .to(RedisRepository.class)
        .in(Singleton.class);
  }

  private RedissonClient initializeRedisClient(RedisStoreConfiguration redisStoreConfiguration) {
    Config config = new Config();
    JsonJacksonCodec jsonJacksonCodec = initializeJsonJacksonCodec();

    SingleServerConfig singleServerConfig = config
        .setCodec(jsonJacksonCodec)
        .useSingleServer()
        .setAddress(redisStoreConfiguration.address());
        if(!Strings.isNullOrEmpty(redisStoreConfiguration.password())) {
          singleServerConfig.setPassword(redisStoreConfiguration.password());
        }
    return Redisson.create(config);
  }

  private JsonJacksonCodec initializeJsonJacksonCodec() {
    JsonJacksonCodec jsonJacksonCodec = JsonJacksonCodec.INSTANCE;
    ObjectMapper objectMapper = jsonJacksonCodec.getObjectMapper();

    objectMapper.addMixIn(ImageData.class, ImageDataMixIn.class);
    objectMapper.addMixIn(ImageLayoutElement.class, ImageLayoutElementMixIn.class);
    objectMapper.addMixIn(LayerGroup.class, LayerGroupMixIn.class);
    objectMapper.addMixIn(Layer.class, LayerMixIn.class);
    objectMapper.addMixIn(ModelLayoutElement.class, ModelLayoutElementMixIn.class);
    objectMapper.addMixIn(ShapeLayoutElement.class, ShapeLayoutElementMixIn.class);
    objectMapper.addMixIn(VisualLayout.class, VisualLayoutMixIn.class);

    objectMapper.addMixIn(Block.class, BlockMixIn.class);
    objectMapper.addMixIn(Layout.class, LayoutMixIn.class);
    objectMapper.addMixIn(Couple.class, CoupleMixIn.class);
    objectMapper.addMixIn(Group.class, GroupMixIn.class);
    objectMapper.addMixIn(Location.class, LocationMixIn.class);
    objectMapper.addMixIn(Link.class, LinkMixIn.class);
    objectMapper.addMixIn(Location.Layout.class, LocationMixIn.LayoutMixIn.class);
    objectMapper.addMixIn(LocationType.class, LocationTypeMixIn.class);
    objectMapper.addMixIn(LocationType.Layout.class, LocationTypeMixIn.LayoutMixIN.class);
    objectMapper.addMixIn(Path.class, PathMixIn.class);
    objectMapper.addMixIn(Path.Layout.class, PathMixIn.LayoutMixIn.class);
    objectMapper.addMixIn(PeripheralInformation.class, PeripheralInformationMixIn.class);
    objectMapper.addMixIn(PlantModel.class, PlantModelMixIn.class);
    objectMapper.addMixIn(Point.class, PointMixIn.class);
    objectMapper.addMixIn(Point.Layout.class, PointMixIn.LayoutMixIn.class);
    objectMapper.addMixIn(TCSResource.class, TCSResourceMixIn.class);
    objectMapper.addMixIn(TCSResourceReference.class, TCSResourceReferenceMixIn.class);
    objectMapper.addMixIn(Triple.class, TripleMixIn.class);
    objectMapper.addMixIn(Vehicle.class, VehicleMixIn.class);

    objectMapper.addMixIn(TCSObjectReference.class, TCSObjectReferenceMixIn.class);

    return jsonJacksonCodec;
  }
}
