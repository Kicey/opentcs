package site.kicey.kernel.extensions.redis.mixin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.awt.Color;
import java.util.Map;
import java.util.Set;
import org.opentcs.data.ObjectHistory;
import org.opentcs.data.model.Block.Layout;
import org.opentcs.data.model.Block.Type;
import org.opentcs.data.model.TCSResourceReference;

/**
 * Mix-in for {@link org.opentcs.data.model.Block}.
 */
public abstract class BlockMixIn {

  @JsonCreator
  private BlockMixIn(@JsonProperty("name") String name,
      @JsonProperty("properties") Map<String, String> properties,
      @JsonProperty("history") ObjectHistory history,
      @JsonProperty("type") Type type,
      @JsonProperty("members") Set<TCSResourceReference<?>> members,
      @JsonProperty("layout") Layout layout) {
  }

  /**
   * Mix-in for {@link org.opentcs.data.model.Block.Layout}.
   */
  public static abstract class LayoutMixIn {

    @JsonCreator
    public LayoutMixIn(@JsonProperty("color") Color color) {
    }
  }
}
