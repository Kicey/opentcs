package site.kicey.kernel.extensions.redis.mixin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.opentcs.data.model.TCSResource;

/**
 * Mix-in for {@link org.opentcs.data.model.TCSResourceReference}.
 * @param <E>
 */
public abstract class TCSResourceReferenceMixIn<E extends TCSResource<E>> {

  @JsonCreator
  protected TCSResourceReferenceMixIn(@JsonProperty("newReferent") TCSResource<E> newReferent) {}
}
