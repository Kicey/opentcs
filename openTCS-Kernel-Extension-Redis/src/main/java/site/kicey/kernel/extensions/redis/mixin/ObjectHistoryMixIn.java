package site.kicey.kernel.extensions.redis.mixin;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.util.List;
import org.opentcs.data.ObjectHistory;

/**
 * Mix-in for {@link org.opentcs.data.ObjectHistory}.
 */
public abstract class ObjectHistoryMixIn {

  @JsonCreator
  private ObjectHistoryMixIn(@JsonProperty("entries") List<ObjectHistory.Entry> entries) {
  }

  /**
   * Mix-in for {@link org.opentcs.data.ObjectHistory.Entry}.
   */
  public static abstract class EntryMixIn {

    @JsonCreator
    public EntryMixIn(@JsonProperty("timestamp") Instant timestamp,
        @JsonProperty("eventCode") String eventCode,
        @JsonProperty("supplement") Object supplement) {
    }
  }
}
