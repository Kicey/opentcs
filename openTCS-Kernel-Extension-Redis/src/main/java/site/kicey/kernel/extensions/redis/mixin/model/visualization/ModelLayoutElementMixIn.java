package site.kicey.kernel.extensions.redis.mixin.model.visualization;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.opentcs.data.TCSObjectReference;
import org.opentcs.util.annotations.ScheduledApiChange;

/**
 * Mix-in for {@link org.opentcs.data.model.visualization.ModelLayoutElement}.
 */
@Deprecated
@ScheduledApiChange(details = "Will be removed.", when = "6.0")
public abstract class ModelLayoutElementMixIn {

  @JsonCreator
  public ModelLayoutElementMixIn(
      @JsonProperty("visualizedObject") TCSObjectReference<?> visualizedObject) {
  }
}
