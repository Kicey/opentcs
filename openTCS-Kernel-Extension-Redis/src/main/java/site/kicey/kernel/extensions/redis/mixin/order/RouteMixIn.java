/**
 * Copyright (c) The openTCS Authors.
 * <p>
 * This program is free software and subject to the MIT license. (For details, see the licensing
 * information (LICENSE.txt) you should have received with this copy of the software.)
 */
package site.kicey.kernel.extensions.redis.mixin.order;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import org.opentcs.data.model.Path;
import org.opentcs.data.model.Point;
import org.opentcs.data.model.Vehicle;
import org.opentcs.data.order.ReroutingType;
import org.opentcs.data.order.Route.Step;

/**
 * Mix-in for {@link org.opentcs.data.order.Route}.
 */
public abstract class RouteMixIn {
  @JsonCreator
  public RouteMixIn(@JsonProperty("steps") List<Step> routeSteps,
      @JsonProperty("costs") long routeCosts) {
  }

  public static abstract class StepMixIn {
    @JsonCreator
    public StepMixIn(@JsonProperty("path") Path path,
        @JsonProperty("srcPoint") Point srcPoint,
        @JsonProperty("destinationPoint") Point destPoint,
        @JsonProperty("vehicleOrientation") Vehicle.Orientation orientation,
        @JsonProperty("routeIndex") int routeIndex,
        @JsonProperty("executionAllowed") boolean executionAllowed,
        @JsonProperty("reroutingType") ReroutingType reroutingType) {
    }
  }
}
