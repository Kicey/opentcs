package site.kicey.strategies.rpc.dispatching;

import javax.annotation.Nonnull;
import org.opentcs.components.kernel.Dispatcher;
import org.opentcs.data.model.Vehicle;
import org.opentcs.data.order.ReroutingType;
import org.opentcs.data.order.TransportOrder;

/**
 * Rpc dispatcher based on Dubbo.
 *
 * @author kicey
 */
public class RpcDispatcher implements Dispatcher {

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
   * Notifies the dispatcher that it should start the dispatching process.
   */
  @Override
  public void dispatch() {

  }

  /**
   * Notifies the dispatcher that the given transport order is to be withdrawn/aborted.
   *
   * @param order          The transport order to be withdrawn/aborted.
   * @param immediateAbort Whether the order should be aborted immediately instead of withdrawn.
   */
  @Override
  public void withdrawOrder(@Nonnull TransportOrder order, boolean immediateAbort) {

  }

  /**
   * Notifies the dispatcher that any order a given vehicle might be processing is to be withdrawn.
   *
   * @param vehicle        The vehicle whose order is withdrawn.
   * @param immediateAbort Whether the vehicle's order should be aborted immediately instead of
   *                       withdrawn.
   */
  @Override
  public void withdrawOrder(@Nonnull Vehicle vehicle, boolean immediateAbort) {

  }

  /**
   * Notifies the dispatcher of changes in the topology.
   */
  @Override
  public void topologyChanged() {

  }

  /**
   * Notifies the dispatcher of a request to reroute the given vehicle considering the given
   * reoruting type.
   *
   * @param vehicle       The vehicle to be rerouted.
   * @param reroutingType The type of the requested rerouting.
   */
  @Override
  public void reroute(@Nonnull Vehicle vehicle, @Nonnull ReroutingType reroutingType) {
    Dispatcher.super.reroute(vehicle, reroutingType);
  }
}
