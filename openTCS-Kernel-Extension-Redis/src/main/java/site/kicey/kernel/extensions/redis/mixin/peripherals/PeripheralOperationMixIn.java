/**
 * Copyright (c) The openTCS Authors.
 * <p>
 * This program is free software and subject to the MIT license. (For details, see the licensing
 * information (LICENSE.txt) you should have received with this copy of the software.)
 */
package site.kicey.kernel.extensions.redis.mixin.peripherals;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.opentcs.data.model.Location;
import org.opentcs.data.model.TCSResourceReference;
import org.opentcs.data.peripherals.PeripheralOperation.ExecutionTrigger;

/**
 * Describes an operation that is to be executed by a peripheral device.
 *
 * @author Martin Grzenia (Fraunhofer IML)
 */
public abstract class PeripheralOperationMixIn {

  @JsonCreator
  public PeripheralOperationMixIn(@JsonProperty("location") TCSResourceReference<Location> location,
      @JsonProperty("operation") String operation,
      @JsonProperty("executionTrigger") ExecutionTrigger executionTrigger,
      @JsonProperty("completionRequired") boolean completionRequired) {
  }
}
