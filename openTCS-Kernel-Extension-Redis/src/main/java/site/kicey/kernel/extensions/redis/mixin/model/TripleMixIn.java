package site.kicey.kernel.extensions.redis.mixin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Mix-in for {@link org.opentcs.data.model.Triple}.
 */
public class TripleMixIn {

  @JsonCreator
  public TripleMixIn(@JsonProperty("x") long x,
      @JsonProperty("y") long y,
      @JsonProperty("z") long z) {}
}
