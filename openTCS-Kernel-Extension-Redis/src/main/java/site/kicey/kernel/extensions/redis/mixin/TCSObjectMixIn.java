package site.kicey.kernel.extensions.redis.mixin;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import javax.annotation.Nonnull;
import org.opentcs.data.ObjectHistory;

/**
 * Mix-in for {@link org.opentcs.data.TCSObject}.
 */
public abstract class TCSObjectMixIn {

  @JsonCreator
  public TCSObjectMixIn(@JsonProperty("name") @Nonnull String objectName,
      @JsonProperty("properties") Map<String, String> properties,
      @JsonProperty("history") ObjectHistory history) {
  }
}
