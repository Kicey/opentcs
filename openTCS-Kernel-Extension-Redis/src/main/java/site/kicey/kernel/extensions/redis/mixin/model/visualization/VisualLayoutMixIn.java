package site.kicey.kernel.extensions.redis.mixin.model.visualization;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.opentcs.data.ObjectHistory;
import org.opentcs.data.model.visualization.Layer;
import org.opentcs.data.model.visualization.LayerGroup;
import org.opentcs.data.model.visualization.LayoutElement;

/**
 * Mix-in for {@link org.opentcs.data.model.visualization.VisualLayout}.
 *
 * @author kicey
 */
public abstract class VisualLayoutMixIn {
  @JsonCreator
  private VisualLayoutMixIn(@JsonProperty("name") String name,
      @JsonProperty("properties") Map<String, String> properties,
      @JsonProperty("history") ObjectHistory history,
      @JsonProperty("scaleX") double scaleX,
      @JsonProperty("scaleY") double scaleY,
      @JsonProperty("layoutElements") Set<LayoutElement> layoutElements,
      @JsonProperty("layers") List<Layer> layers,
      @JsonProperty("layerGroups") List<LayerGroup> layerGroups){}
}
