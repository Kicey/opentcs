package site.kicey.kernel.extensions.redis.mixin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;
import org.opentcs.data.ObjectHistory;
import org.opentcs.data.model.LocationType;
import org.opentcs.data.model.LocationType.Layout;
import org.opentcs.data.model.visualization.LocationRepresentation;

/**
 * Mix-in for {@link LocationType}.
 */
public abstract class LocationTypeMixIn {

  @JsonCreator
  private LocationTypeMixIn(@JsonProperty("name") String name,
                       @JsonProperty("properties") Map<String, String> properties,
                       @JsonProperty("history") ObjectHistory history,
                       @JsonProperty("allowedOperations") List<String> allowedOperations,
                       @JsonProperty("allowedPeripheralOperations") List<String> allowedPeripheralOperations,
                       @JsonProperty("layout") Layout layout) {}

  public static abstract class LayoutMixIN {
    @JsonCreator
    public LayoutMixIN(@JsonProperty("locationRepresentation") LocationRepresentation locationRepresentation) {
    }
  }
}
