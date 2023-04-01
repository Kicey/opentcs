package site.kicey.kernel.extensions.redis.mixin.model.visualization;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Mix-in for {@link org.opentcs.data.model.visualization.LayerGroup}.
 *
 * @author kicey
 */
public abstract class LayerGroupMixIn {

  @JsonCreator
  public LayerGroupMixIn(@JsonProperty("id") int id, @JsonProperty("name") String name,
      @JsonProperty("visible") boolean visible) {
  }
}
