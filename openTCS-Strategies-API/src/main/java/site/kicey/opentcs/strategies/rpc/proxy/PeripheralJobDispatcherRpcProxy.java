package site.kicey.opentcs.strategies.rpc.proxy;

import javax.annotation.Nonnull;
import org.opentcs.components.kernel.PeripheralJobDispatcher;
import org.opentcs.data.model.Location;
import org.opentcs.data.peripherals.PeripheralJob;

public class PeripheralJobDispatcherRpcProxy implements PeripheralJobDispatcher {

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
  public void withdrawJob(@Nonnull Location location) throws IllegalArgumentException {

  }

  @Override
  public void withdrawJob(@Nonnull PeripheralJob job) throws IllegalArgumentException {

  }
}
