package site.kicey.kernel.extensions.redis.mixin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.opentcs.data.TCSObjectReference;
import org.opentcs.data.model.PeripheralInformation;
import org.opentcs.data.model.PeripheralInformation.ProcState;
import org.opentcs.data.model.PeripheralInformation.State;
import org.opentcs.data.peripherals.PeripheralJob;

/**
 * Mix-in for {@link PeripheralInformation}.
 */
public class PeripheralInformationMixIn {

  @JsonCreator
  private PeripheralInformationMixIn(
      @JsonProperty("reservationToken") String reservationToken,
      @JsonProperty("state") State state,
      @JsonProperty("procState") ProcState procState,
      @JsonProperty("peripheralJob") TCSObjectReference<PeripheralJob> peripheralJob) {}
}
