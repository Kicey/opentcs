/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package site.kicey.kernel.extensions.redis.mixin.peripherals;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.util.Map;
import org.opentcs.data.ObjectHistory;
import org.opentcs.data.TCSObjectReference;
import org.opentcs.data.model.Vehicle;
import org.opentcs.data.order.TransportOrder;
import org.opentcs.data.peripherals.PeripheralJob;
import org.opentcs.data.peripherals.PeripheralJob.State;
import org.opentcs.data.peripherals.PeripheralOperation;

/**
 * Mix-in for {@link PeripheralJob}.
 */
public abstract class PeripheralJobMixIn {
  @JsonCreator
  public PeripheralJobMixIn(@JsonProperty("name") String name,
                       @JsonProperty("reservationToken") String reservationToken,
                       @JsonProperty("peripheralOperation") PeripheralOperation peripheralOperation) {}
  @JsonCreator
  private PeripheralJobMixIn(@JsonProperty("objectName") String objectName,
                        @JsonProperty("properties") Map<String, String> properties,
                        @JsonProperty("history") ObjectHistory history,
                        @JsonProperty("reservationToken") String reservationToken,
                        @JsonProperty("relatedVehicle") TCSObjectReference<Vehicle> relatedVehicle,
                        @JsonProperty("transportOrder") TCSObjectReference<TransportOrder> transportOrder,
                        @JsonProperty("peripheralOperation") PeripheralOperation peripheralOperation,
                        @JsonProperty("state") State state,
                        @JsonProperty("creationTime") Instant creationTime,
                        @JsonProperty("finishedTime") Instant finishedTime) {}

}
