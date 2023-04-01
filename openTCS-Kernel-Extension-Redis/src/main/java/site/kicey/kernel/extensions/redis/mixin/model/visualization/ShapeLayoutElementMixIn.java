package site.kicey.kernel.extensions.redis.mixin.model.visualization;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Mix-in for {@link org.opentcs.data.model.visualization.ShapeLayoutElement}.
 */
public abstract class ShapeLayoutElementMixIn {

  @JsonCreator
  public ShapeLayoutElementMixIn() {}
}
