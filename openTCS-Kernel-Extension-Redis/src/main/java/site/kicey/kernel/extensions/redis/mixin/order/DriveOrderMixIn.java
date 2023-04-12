/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package site.kicey.kernel.extensions.redis.mixin.order;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import org.opentcs.data.TCSObjectReference;
import org.opentcs.data.order.DriveOrder.Destination;
import org.opentcs.data.order.DriveOrder.State;
import org.opentcs.data.order.Route;
import org.opentcs.data.order.TransportOrder;

/**
 * Mix-in for {@link org.opentcs.data.order.DriveOrder}.
 */
public abstract class DriveOrderMixIn {
  @JsonCreator
  private DriveOrderMixIn(@JsonProperty("destination") Destination destination,
                     @JsonProperty("transportOrder") TCSObjectReference<TransportOrder> transportOrder,
                     @JsonProperty("route") Route route,
                     @JsonProperty("state") State state) {
  }


  /**
   * Mix-in for {@link org.opentcs.data.order.DriveOrder.Destination}.
   */
  public static abstract class DestinationMixIn {
    @JsonCreator
    private DestinationMixIn(@JsonProperty("destination") TCSObjectReference<?> destination,
                        @JsonProperty("properties") Map<String, String> properties,
                        @JsonProperty("operation") String operation) {
    }
  }
}
