package site.kicey.opentcs.strategies.rpc.proxy;

import javax.annotation.Nonnull;
import org.opentcs.components.kernel.Dispatcher;
import org.opentcs.data.model.Vehicle;
import org.opentcs.data.order.ReroutingType;
import org.opentcs.data.order.TransportOrder;

public class DispatcherRpcProxy implements Dispatcher {

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
  public void dispatch() {

  }

  @Override
  public void withdrawOrder(@Nonnull TransportOrder order, boolean immediateAbort) {

  }

  @Override
  public void withdrawOrder(@Nonnull Vehicle vehicle, boolean immediateAbort) {

  }

  @Override
  public void topologyChanged() {

  }

  @Override
  public void reroute(@Nonnull Vehicle vehicle, @Nonnull ReroutingType reroutingType) {
    Dispatcher.super.reroute(vehicle, reroutingType);
  }
}
