/**
 * Copyright (c) The openTCS Authors.
 * <p>
 * This program is free software and subject to the MIT license. (For details, see the licensing
 * information (LICENSE.txt) you should have received with this copy of the software.)
 */
package site.kicey.kernel.extensions.redis.mixin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import java.util.Set;
import org.opentcs.data.ObjectHistory;
import org.opentcs.data.TCSObjectReference;
import org.opentcs.data.model.Couple;
import org.opentcs.data.model.Location;
import org.opentcs.data.model.Path;
import org.opentcs.data.model.Point;
import org.opentcs.data.model.Point.Layout;
import org.opentcs.data.model.Triple;
import org.opentcs.data.model.Vehicle;

/**
 * Mix-in for {@link org.opentcs.data.model.Point}.
 */
public abstract class PointMixIn {
  @JsonCreator
  private PointMixIn(@JsonProperty("name") String name,
      @JsonProperty("properties") Map<String, String> properties,
      @JsonProperty("history") ObjectHistory history,
      @JsonProperty("position") Triple position,
      @JsonProperty("type") Point.Type type,
      @JsonProperty("vehicleOrientationAngle") double vehicleOrientationAngle,
      @JsonProperty("incomingPaths") Set<TCSObjectReference<Path>> incomingPaths,
      @JsonProperty("outgoingPaths") Set<TCSObjectReference<Path>> outgoingPaths,
      @JsonProperty("attachedLinks") Set<Location.Link> attachedLinks,
      @JsonProperty("occupyingVehicle") TCSObjectReference<Vehicle> occupyingVehicle,
      @JsonProperty("layout") Layout layout) {
  }

  /**
   * Mix-in for {@link org.opentcs.data.model.Point.Layout}.
   */
  public abstract static class LayoutMixIn {
    @JsonCreator
    public LayoutMixIn(@JsonProperty("position") Couple position,
        @JsonProperty("labelOffset") Couple labelOffset,
        @JsonProperty("layerId") int layerId) {
    }
  }
}
