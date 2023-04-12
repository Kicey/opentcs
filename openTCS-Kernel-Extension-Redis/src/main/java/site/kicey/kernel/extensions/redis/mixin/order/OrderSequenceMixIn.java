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
import java.util.Map;
import org.opentcs.data.ObjectHistory;
import org.opentcs.data.TCSObjectReference;
import org.opentcs.data.model.Vehicle;
import org.opentcs.data.order.OrderSequence;
import org.opentcs.data.order.TransportOrder;

/**
 * Mix-in for {@link OrderSequence}.
 */
public abstract class OrderSequenceMixIn {

  @JsonCreator
  public OrderSequenceMixIn(@JsonProperty("name") String name) {
  }

  @JsonCreator
  private OrderSequenceMixIn(@JsonProperty("name") String name,
      @JsonProperty("properties") Map<String, String> properties,
      @JsonProperty("history") ObjectHistory history,
      @JsonProperty("type") String type,
      @JsonProperty("intendedVehicle") TCSObjectReference<Vehicle> intendedVehicle,
      @JsonProperty("orders") List<TCSObjectReference<TransportOrder>> orders,
      @JsonProperty("finishedIndex") int finishedIndex,
      @JsonProperty("complete") boolean complete,
      @JsonProperty("failureFatal") boolean failureFatal,
      @JsonProperty("finished") boolean finished,
      @JsonProperty("processingVehicle") TCSObjectReference<Vehicle> processingVehicle) {
  }
}
