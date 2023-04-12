package site.kicey.kernel.extensions.redis.mixin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import java.util.Set;
import org.opentcs.data.model.Block;
import org.opentcs.data.model.Location;
import org.opentcs.data.model.LocationType;
import org.opentcs.data.model.Path;
import org.opentcs.data.model.PlantModel;
import org.opentcs.data.model.Point;
import org.opentcs.data.model.Vehicle;
import org.opentcs.data.model.visualization.VisualLayout;

/**
 * Mix-in for {@link PlantModel}.
 */
public abstract class PlantModelMixIn {

  @JsonCreator
  private PlantModelMixIn(@JsonProperty("name") String name,
      @JsonProperty("properties") Map<String, String> properties,
      @JsonProperty("points") Set<Point> points,
      @JsonProperty("paths") Set<Path> paths,
      @JsonProperty("locationTypes") Set<LocationType> locationTypes,
      @JsonProperty("locations") Set<Location> locations,
      @JsonProperty("blocks") Set<Block> blocks,
      @JsonProperty("vehicles") Set<Vehicle> vehicles,
      @JsonProperty("visualLayouts") Set<VisualLayout> visualLayouts) {
  }
}
