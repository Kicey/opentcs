package site.kicey.kernel.extensions.redis.mixin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import org.opentcs.data.ObjectHistory;
import org.opentcs.data.model.TCSResource;

/**
 * Mix-in for {@link org.opentcs.data.model.TCSResource}.
 *
 * @param <E>
 */
public abstract class TCSResourceMixIn<E extends TCSResource<E>> {
  @JsonCreator
  protected TCSResourceMixIn(@JsonProperty("name") String name,
      @JsonProperty("properties") Map<String, String> properties,
      @JsonProperty("history") ObjectHistory history) {
  }
}
