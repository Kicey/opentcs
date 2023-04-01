package site.kicey.kernel.extensions.redis.mixin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import java.util.Set;
import org.opentcs.data.ObjectHistory;
import org.opentcs.data.TCSObjectReference;
import org.opentcs.data.model.Couple;
import org.opentcs.data.model.Location;
import org.opentcs.data.model.Location.Layout;
import org.opentcs.data.model.Location.Link;
import org.opentcs.data.model.LocationType;
import org.opentcs.data.model.PeripheralInformation;
import org.opentcs.data.model.Point;
import org.opentcs.data.model.TCSResourceReference;
import org.opentcs.data.model.Triple;
import org.opentcs.data.model.visualization.LocationRepresentation;

/**
 * Mix-in for {@link org.opentcs.data.model.Location}.
 */
public abstract class LocationMixIn {

  @JsonCreator
  private LocationMixIn(@JsonProperty("name") String name,
      @JsonProperty("properties") Map<String, String> properties,
      @JsonProperty("history") ObjectHistory history,
      @JsonProperty("locationType") TCSObjectReference<LocationType> locationType,
      @JsonProperty("position") Triple position,
      @JsonProperty("attachedLinks") Set<Link> attachedLinks,
      @JsonProperty("locked") boolean locked,
      @JsonProperty("peripheralInformation") PeripheralInformation peripheralInformation,
      @JsonProperty("layout") Layout layout) {
  }


  /**
   * Mix-in for {@link org.opentcs.data.model.Location.Link}.
   */
  public static abstract class LinkMixIn {

    @JsonCreator
    private LinkMixIn(@JsonProperty("location") TCSResourceReference<Location> location,
        @JsonProperty("point") TCSResourceReference<Point> point,
        @JsonProperty("allowedOperations") Set<String> allowedOperations) {
    }
  }

  /**
   *
   */
  public static abstract class LayoutMixIn {

    @JsonCreator
    public LayoutMixIn(@JsonProperty("position") Couple position,
        @JsonProperty("labelOffset") Couple labelOffset,
        @JsonProperty("locationRepresentation") LocationRepresentation locationRepresentation,
        @JsonProperty("layerId") int layerId) {
    }
  }
}
