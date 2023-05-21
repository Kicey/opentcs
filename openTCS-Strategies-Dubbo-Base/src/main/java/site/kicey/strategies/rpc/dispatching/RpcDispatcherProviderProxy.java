package site.kicey.strategies.rpc.dispatching;

import com.google.inject.Inject;
import org.opentcs.components.kernel.Dispatcher;
import org.opentcs.data.model.Vehicle;
import org.opentcs.data.order.ReroutingType;
import org.opentcs.data.order.TransportOrder;
import site.kicey.opentcs.strategies.rpc.api.RpcDispatcher;

import javax.annotation.Nonnull;

public class RpcDispatcherProviderProxy implements RpcDispatcher {

  private final Dispatcher dispatcher;

  @Inject
  public RpcDispatcherProviderProxy(Dispatcher dispatcher){
    this.dispatcher = dispatcher;
  }

  @Override
  public void initialize() {
    dispatcher.initialize();
  }

  @Override
  public boolean isInitialized() {
    return dispatcher.isInitialized();
  }

  @Override
  public void terminate() {
    dispatcher.terminate();
  }

  @Override
  public void dispatch() {
    dispatcher.dispatch();
  }

  @Override
  public void withdrawOrder(@Nonnull TransportOrder order, boolean immediateAbort) {
    dispatcher.withdrawOrder(order, immediateAbort);
  }

  @Override
  public void withdrawOrder(@Nonnull Vehicle vehicle, boolean immediateAbort) {
    dispatcher.withdrawOrder(vehicle, immediateAbort);
  }

  @Override
  public void topologyChanged() {
    dispatcher.topologyChanged();
  }

  @Override
  public void reroute(@Nonnull Vehicle vehicle, @Nonnull ReroutingType reroutingType) {
    dispatcher.reroute(vehicle, reroutingType);
  }
}
