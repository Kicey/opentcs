package site.kicey.strategies.rpc.routing;

import com.google.inject.Inject;
import org.opentcs.components.kernel.Router;
import org.opentcs.data.TCSObjectReference;
import org.opentcs.data.model.Point;
import org.opentcs.data.model.Vehicle;
import org.opentcs.data.order.DriveOrder;
import org.opentcs.data.order.Route;
import org.opentcs.data.order.TransportOrder;
import site.kicey.opentcs.strategies.rpc.api.RpcRouter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class RpcRouterProviderProxy implements RpcRouter {

  private final Router router;

  @Inject
  public RpcRouterProviderProxy(Router router) {
    this.router = router;
  }

  @Override
  public void initialize() {
    router.initialize();
  }

  @Override
  public boolean isInitialized() {
    return router.isInitialized();
  }

  @Override
  public void terminate() {
    router.terminate();
  }

  @Override
  public void topologyChanged() {
    router.topologyChanged();
  }

  @Nonnull
  @Override
  public Set<Vehicle> checkRoutability(@Nonnull TransportOrder order) {
    return router.checkRoutability(order);
  }

  @Nonnull
  @Override
  public Optional<List<DriveOrder>> getRoute(@Nonnull Vehicle vehicle, @Nonnull Point sourcePoint, @Nonnull TransportOrder transportOrder) {
    return router.getRoute(vehicle, sourcePoint, transportOrder);
  }

  @Nonnull
  @Override
  public Optional<Route> getRoute(@Nonnull Vehicle vehicle, @Nonnull Point sourcePoint, @Nonnull Point destinationPoint) {
    return router.getRoute(vehicle, sourcePoint, destinationPoint);
  }

  @Override
  public long getCosts(@Nonnull Vehicle vehicle, @Nonnull Point sourcePoint, @Nonnull Point destinationPoint) {
    return router.getCosts(vehicle, sourcePoint, destinationPoint);
  }

  @Override
  public long getCostsByPointRef(@Nonnull Vehicle vehicle, @Nonnull TCSObjectReference<Point> srcPointRef, @Nonnull TCSObjectReference<Point> dstPointRef) {
    return router.getCostsByPointRef(vehicle, srcPointRef, dstPointRef);
  }

  @Override
  public void selectRoute(@Nonnull Vehicle vehicle, @Nullable List<DriveOrder> driveOrders) {
    router.selectRoute(vehicle, driveOrders);
  }

  @Nonnull
  @Override
  public Map<Vehicle, List<DriveOrder>> getSelectedRoutes() {
    return router.getSelectedRoutes();
  }

  @Nonnull
  @Override
  public Set<Point> getTargetedPoints() {
    return router.getTargetedPoints();
  }
}
