package site.kicey.strategies.rpc.routing;

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

/**
 * Rpc router based on Dubbo.
 */
public class RpcRouter implements Router {

  /**
   * (Re-)Initializes this component before it is being used.
   */
  @Override
  public void initialize() {

  }

  /**
   * Checks whether this component is initialized.
   *
   * @return <code>true</code> if, and only if, this component is initialized.
   */
  @Override
  public boolean isInitialized() {
    return false;
  }

  /**
   * Terminates the instance and frees resources.
   */
  @Override
  public void terminate() {

  }

  /**
   * Notifies the router of changes in the topology.
   */
  @Override
  public void topologyChanged() {

  }

  /**
   * Checks the general routability of a given transport order.
   *
   * @param order The transport order to check for routability.
   * @return A set of vehicles for which a route for the given transport order would be computable.
   */
  @Nonnull
  @Override
  public Set<Vehicle> checkRoutability(@Nonnull TransportOrder order) {
    return null;
  }

  /**
   * Returns a complete route for a given vehicle that starts on a specified point and allows the
   * vehicle to process a given transport order. The route is encapsulated into drive orders which
   * correspond to those drive orders that the transport order is composed of. The transport order
   * itself is not modified.
   *
   * @param vehicle        The vehicle for which the calculated route must be passable.
   * @param sourcePoint    The position at which the vehicle would start processing the transport
   *                       order (i.e. the vehicle's current position).
   * @param transportOrder The transport order to be processed by the vehicle.
   * @return A list of drive orders containing the complete calculated route for the given transport
   * order, passable the given vehicle and starting on the given point, or the empty optional, if no
   * such route exists.
   */
  @Nonnull
  @Override
  public Optional<List<DriveOrder>> getRoute(@Nonnull Vehicle vehicle, @Nonnull Point sourcePoint,
      @Nonnull TransportOrder transportOrder) {
    return Optional.empty();
  }

  /**
   * Returns a route from one point to another, passable for a given vehicle.
   *
   * @param vehicle          The vehicle for which the route must be passable.
   * @param sourcePoint      The starting point of the route to calculate.
   * @param destinationPoint The end point of the route to calculate.
   * @return The calculated route, or the empty optional, if a route between the given points does
   * not exist.
   */
  @Nonnull
  @Override
  public Optional<Route> getRoute(@Nonnull Vehicle vehicle, @Nonnull Point sourcePoint,
      @Nonnull Point destinationPoint) {
    return Optional.empty();
  }

  /**
   * Returns the costs for travelling a route from one point to another with a given vehicle.
   *
   * @param vehicle          The vehicle for which the route must be passable.
   * @param sourcePoint      The starting point of the route.
   * @param destinationPoint The end point of the route.
   * @return The costs of the route, or <code>Long.MAX_VALUE</code>, if no such route exists.
   */
  @Override
  public long getCosts(@Nonnull Vehicle vehicle, @Nonnull Point sourcePoint,
      @Nonnull Point destinationPoint) {
    return 0;
  }

  /**
   * Returns the costs for travelling a route from one point to another with a given vehicle.
   *
   * @param vehicle     The vehicle for which the route must be passable.
   * @param srcPointRef The starting point reference of the route.
   * @param dstPointRef The end point reference of the route.
   * @return The costs of the route, or <code>Long.MAX_VALUE</code>, if no such route exists.
   */
  @Override
  public long getCostsByPointRef(@Nonnull Vehicle vehicle,
      @Nonnull TCSObjectReference<Point> srcPointRef,
      @Nonnull TCSObjectReference<Point> dstPointRef) {
    return 0;
  }

  /**
   * Notifies the router of a route being selected for a vehicle.
   *
   * @param vehicle     The vehicle for which a route is being selected.
   * @param driveOrders The drive orders encapsulating the route being selected, or
   *                    <code>null</code>, if no route is being selected for the vehicle (i.e. an
   *                    existing entry for the given vehicle would be removed).
   */
  @Override
  public void selectRoute(@Nonnull Vehicle vehicle, @Nullable List<DriveOrder> driveOrders) {

  }

  /**
   * Returns an unmodifiable view on the selected routes the router knows about. The returned map
   * contains an entry for each vehicle for which a selected route is known.
   *
   * @return An unmodifiable view on the selected routes the router knows about.
   */
  @Nonnull
  @Override
  public Map<Vehicle, List<DriveOrder>> getSelectedRoutes() {
    return null;
  }

  /**
   * Returns all points which are currently targeted by any vehicle.
   *
   * @return A set of all points currently targeted by any vehicle.
   */
  @Nonnull
  @Override
  public Set<Point> getTargetedPoints() {
    return null;
  }
}
