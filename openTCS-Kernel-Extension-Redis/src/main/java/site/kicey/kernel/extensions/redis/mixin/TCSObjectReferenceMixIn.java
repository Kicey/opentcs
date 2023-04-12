package site.kicey.kernel.extensions.redis.mixin;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Mix-in for {@link org.opentcs.data.TCSObjectReference}.
 *
 * @author kicey
 */
public abstract class TCSObjectReferenceMixIn {
  @JsonCreator
  public TCSObjectReferenceMixIn(@JsonProperty("referentClass") Class<?> clazz,
      @JsonProperty("name") String newName) {}
}
