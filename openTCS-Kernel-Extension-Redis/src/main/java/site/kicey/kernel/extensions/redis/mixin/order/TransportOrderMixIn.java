package site.kicey.kernel.extensions.redis.mixin.order;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.opentcs.data.ObjectHistory;
import org.opentcs.data.TCSObjectReference;
import org.opentcs.data.model.Vehicle;
import org.opentcs.data.order.DriveOrder;
import org.opentcs.data.order.OrderSequence;
import org.opentcs.data.order.TransportOrder;
import org.opentcs.data.order.TransportOrder.State;

/**
 * Mix-in for {@link TransportOrder}.
 */
public abstract class TransportOrderMixIn {

  @JsonCreator
  public TransportOrderMixIn(@JsonProperty("name") String name,
      @JsonProperty("driveOrders") List<DriveOrder> driveOrders) {
  }

  @JsonCreator
  private TransportOrderMixIn(@JsonProperty("name") String name,
      @JsonProperty("properties") Map<String, String> properties,
      @JsonProperty("history") ObjectHistory history,
      @JsonProperty("type") String type,
      @JsonProperty("driveOrders") List<DriveOrder> driveOrders,
      @JsonProperty("peripheralReservationToken") String peripheralReservationToken,
      @JsonProperty("currentDriveOrderIndex") int currentDriveOrderIndex,
      @JsonProperty("creationTime") Instant creationTime,
      @JsonProperty("intendedVehicle") TCSObjectReference<Vehicle> intendedVehicle,
      @JsonProperty("deadline") Instant deadline,
      @JsonProperty("dispensable") boolean dispensable,
      @JsonProperty("wrappingSequence") TCSObjectReference<OrderSequence> wrappingSequence,
      @JsonProperty("dependencies") Set<TCSObjectReference<TransportOrder>> dependencies,
      @JsonProperty("processingVehicle") TCSObjectReference<Vehicle> processingVehicle,
      @JsonProperty("state") State state,
      @JsonProperty("finishedTime") Instant finishedTime) {
  }
}
