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
  public RouteMixIn(@JsonProperty("routeSteps") List<Step> routeSteps,
      @JsonProperty("routeCosts") long routeCosts) {
  }


  public static abstract class StepMixIn {

    @JsonCreator
    public StepMixIn(@JsonProperty("path") Path path,
        @JsonProperty("srcPoint") Point srcPoint,
        @JsonProperty("destPoint") Point destPoint,
        @JsonProperty("orientation") Vehicle.Orientation orientation,
        @JsonProperty("routeIndex") int routeIndex,
        @JsonProperty("executionAllowed") boolean executionAllowed,
        @JsonProperty("reroutingType") ReroutingType reroutingType) {
    }

    @JsonCreator
    public StepMixIn(@JsonProperty("path") Path path,
        @JsonProperty("srcPoint") Point srcPoint,
        @JsonProperty("destPoint") Point destPoint,
        @JsonProperty("orientation") Vehicle.Orientation orientation,
        @JsonProperty("routeIndex") int routeIndex,
        @JsonProperty("executionAllowed") boolean executionAllowed) {
    }

    @JsonCreator
    public StepMixIn(@JsonProperty("path") Path path,
        @JsonProperty("srcPoint") Point srcPoint,
        @JsonProperty("destPoint") Point destPoint,
        @JsonProperty("orientation") Vehicle.Orientation orientation,
        @JsonProperty("routeIndex") int routeIndex) {
    }
  }
}
