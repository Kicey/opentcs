package site.kicey.kernel.extensions.redis.mixin;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.annotation.Nonnull;

/**
 * Mix-in for {@link org.opentcs.data.TCSObjectReference}.
 *
 * @author kicey
 */
public abstract class TCSObjectReferenceMixIn {
  @JsonCreator
  private TCSObjectReferenceMixIn(@JsonProperty("referentClass") @Nonnull Class<?> clazz,
      @JsonProperty("name") @Nonnull String newName) {}
}
