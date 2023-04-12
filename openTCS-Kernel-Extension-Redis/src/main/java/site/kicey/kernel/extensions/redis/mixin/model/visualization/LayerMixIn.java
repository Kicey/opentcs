package site.kicey.kernel.extensions.redis.mixin.model.visualization;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Mix-in for {@link org.opentcs.data.model.visualization.Layer}.
 *
 * @author kicey
 */
public class LayerMixIn {
  @JsonCreator
  public LayerMixIn(@JsonProperty("id") int id,
      @JsonProperty("ordinal") int ordinal,
      @JsonProperty("visible") boolean visible,
      @JsonProperty("name")String name,
      @JsonProperty("groupId") int groupId) {}
}
