package site.kicey.kernel.extensions.redis.mixin.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.awt.Color;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.opentcs.data.ObjectHistory;
import org.opentcs.data.TCSObjectReference;
import org.opentcs.data.model.Point;
import org.opentcs.data.model.TCSResourceReference;
import org.opentcs.data.model.Triple;
import org.opentcs.data.model.Vehicle.IntegrationLevel;
import org.opentcs.data.model.Vehicle.Layout;
import org.opentcs.data.model.Vehicle.ProcState;
import org.opentcs.data.model.Vehicle.State;
import org.opentcs.data.order.OrderSequence;
import org.opentcs.data.order.TransportOrder;
import org.opentcs.drivers.vehicle.LoadHandlingDevice;

/**
 * Mix-in for {@link org.opentcs.data.model.Vehicle}.
 */
public abstract class VehicleMixIn {
  @JsonCreator
  private VehicleMixIn(@JsonProperty("name") String name,
                  @JsonProperty("properties") Map<String, String> properties,
                  @JsonProperty("history") ObjectHistory history,
                  @JsonProperty("length") int length,
                  @JsonProperty("energyLevelGood") int energyLevelGood,
                  @JsonProperty("energyLevelCritical") int energyLevelCritical,
                  @JsonProperty("energyLevelFullyRecharged") int energyLevelFullyRecharged,
                  @JsonProperty("energyLevelSufficientlyRecharged") int energyLevelSufficientlyRecharged,
                  @JsonProperty("maxVelocity") int maxVelocity,
                  @JsonProperty("maxReverseVelocity") int maxReverseVelocity,
                  @JsonProperty("rechargeOperation") String rechargeOperation,
                  @JsonProperty("procState") ProcState procState,
                  @JsonProperty("transportOrder") TCSObjectReference<TransportOrder> transportOrder,
                  @JsonProperty("orderSequence") TCSObjectReference<OrderSequence> orderSequence,
                  @JsonProperty("allowedOrderTypes") Set<String> allowedOrderTypes,
                  @JsonProperty("routeProgressIndex") int routeProgressIndex,
                  @JsonProperty("claimedResources") List<Set<TCSResourceReference<?>>> claimedResources,
                  @JsonProperty("allocatedResources") List<Set<TCSResourceReference<?>>> allocatedResources,
                  @JsonProperty("state") State state,
                  @JsonProperty("integrationLevel") IntegrationLevel integrationLevel,
                  @JsonProperty("paused") boolean paused,
                  @JsonProperty("currentPosition") TCSObjectReference<Point> currentPosition,
                  @JsonProperty("nextPosition") TCSObjectReference<Point> nextPosition,
                  @JsonProperty("precisePosition") Triple precisePosition,
                  @JsonProperty("orientationAngle") double orientationAngle,
                  @JsonProperty("energyLevel") int energyLevel,
                  @JsonProperty("loadHandlingDevices") List<LoadHandlingDevice> loadHandlingDevices,
                  @JsonProperty("layout") Layout layout) {}

  @JsonIgnore
  public abstract boolean isEnergyLevelCritical();

  @JsonIgnore
  public abstract boolean isEnergyLevelDegraded();

  @JsonIgnore
  public abstract boolean isEnergyLevelGood();

  @JsonIgnore
  public abstract boolean isEnergyLevelFullyRecharged();

  @JsonIgnore
  public abstract boolean isEnergyLevelSufficientlyRecharged();

  /**
   * Mix-in for {@link org.opentcs.data.model.Vehicle.Layout}.
   */
  public static abstract class LayoutMixIn {

    @JsonCreator
    public LayoutMixIn(@JsonProperty("routeColor") Color routeColor) {}

  }
}
