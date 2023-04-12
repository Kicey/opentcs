package site.kicey.kernel.extensions.redis.mixin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.opentcs.data.model.Couple;

/**
 * Mix-in for {@link Couple}.
 */
public class CoupleMixIn {

  @JsonCreator
  public CoupleMixIn(@JsonProperty("x") long x,
      @JsonProperty("y") long y) {
  }
}
