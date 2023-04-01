package site.kicey.kernel.extensions.redis.mixin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import java.util.Set;
import org.opentcs.data.ObjectHistory;
import org.opentcs.data.TCSObjectReference;

/**
 * Mix-in for {@link org.opentcs.data.model.Group}.
 */
public abstract class GroupMixIn {

  @JsonCreator
  private GroupMixIn(@JsonProperty("name") String name,
      @JsonProperty("properties") Map<String, String> properties,
      @JsonProperty("history") ObjectHistory history,
      @JsonProperty("members") Set<TCSObjectReference<?>> members) {}
}
