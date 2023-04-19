package site.kicey.opentcs.strategies.rpc.proxy;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.opentcs.components.kernel.Router;
import org.opentcs.data.TCSObjectReference;
import org.opentcs.data.model.Point;
import org.opentcs.data.model.Vehicle;
import org.opentcs.data.order.DriveOrder;
import org.opentcs.data.order.Route;
import org.opentcs.data.order.TransportOrder;

public class RouterRpcProxy implements Router {

  @Override
  public void initialize() {

  }

  @Override
  public boolean isInitialized() {
    return false;
  }

  @Override
  public void terminate() {

  }

  @Override
  public void topologyChanged() {

  }

  @Nonnull
  @Override
  public Set<Vehicle> checkRoutability(@Nonnull TransportOrder order) {
    return null;
  }

  @Nonnull
  @Override
  public Optional<List<DriveOrder>> getRoute(@Nonnull Vehicle vehicle, @Nonnull Point sourcePoint,
      @Nonnull TransportOrder transportOrder) {
    return Optional.empty();
  }

  @Nonnull
  @Override
  public Optional<Route> getRoute(@Nonnull Vehicle vehicle, @Nonnull Point sourcePoint,
      @Nonnull Point destinationPoint) {
    return Optional.empty();
  }

  @Override
  public long getCosts(@Nonnull Vehicle vehicle, @Nonnull Point sourcePoint,
      @Nonnull Point destinationPoint) {
    return 0;
  }

  @Override
  public long getCostsByPointRef(@Nonnull Vehicle vehicle,
      @Nonnull TCSObjectReference<Point> srcPointRef,
      @Nonnull TCSObjectReference<Point> dstPointRef) {
    return 0;
  }

  @Override
  public void selectRoute(@Nonnull Vehicle vehicle, @Nullable List<DriveOrder> driveOrders) {

  }

  @Nonnull
  @Override
  public Map<Vehicle, List<DriveOrder>> getSelectedRoutes() {
    return null;
  }

  @Nonnull
  @Override
  public Set<Point> getTargetedPoints() {
    return null;
  }
}
