/**
 * Copyright (c) The openTCS Authors.
 * <p>
 * This program is free software and subject to the MIT license. (For details, see the licensing
 * information (LICENSE.txt) you should have received with this copy of the software.)
 */
package site.kicey.kernel.extensions.redis.mixin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;
import org.opentcs.data.ObjectHistory;
import org.opentcs.data.TCSObjectReference;
import org.opentcs.data.model.Couple;
import org.opentcs.data.model.Path.Layout;
import org.opentcs.data.model.Path.Layout.ConnectionType;
import org.opentcs.data.model.Point;
import org.opentcs.data.peripherals.PeripheralOperation;

/**
 * Mix-in for {@link org.opentcs.data.model.Path}.
 */
public class PathMixIn {

  @JsonCreator
  private PathMixIn(@JsonProperty("name") String name,
      @JsonProperty("properties") Map<String, String> properties,
      @JsonProperty("history") ObjectHistory history,
      @JsonProperty("sourcePoint") TCSObjectReference<Point> sourcePoint,
      @JsonProperty("destinationPoint") TCSObjectReference<Point> destinationPoint,
      @JsonProperty("length") long length,
      @JsonProperty("maxVelocity") int maxVelocity,
      @JsonProperty("maxReverseVelocity") int maxReverseVelocity,
      @JsonProperty("peripheralOperations") List<PeripheralOperation> peripheralOperations,
      @JsonProperty("locked") boolean locked,
      @JsonProperty("layout") Layout layout) {
  }

  /**
   * Mix-in for {@link org.opentcs.data.model.Path.Layout}.
   */
  public static abstract class LayoutMixIn {

    @JsonCreator
    public LayoutMixIn(@JsonProperty("connectionType") ConnectionType connectionType,
        @JsonProperty("controlPoints") List<Couple> controlPoints,
        @JsonProperty("layerId") int layerId) {
    }
  }
}
