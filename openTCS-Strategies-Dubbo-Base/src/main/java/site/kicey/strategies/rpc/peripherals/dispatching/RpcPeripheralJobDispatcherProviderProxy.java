package site.kicey.strategies.rpc.peripherals.dispatching;

import com.google.inject.Inject;
import org.opentcs.components.kernel.PeripheralJobDispatcher;
import org.opentcs.data.model.Location;
import org.opentcs.data.peripherals.PeripheralJob;
import org.opentcs.drivers.peripherals.PeripheralJobCallback;
import site.kicey.opentcs.strategies.rpc.api.RpcPeripheralJobDispatcher;

import javax.annotation.Nonnull;

public class RpcPeripheralJobDispatcherProviderProxy implements RpcPeripheralJobDispatcher {

  private final PeripheralJobDispatcher peripheralJobDispatcher;

  private final PeripheralJobCallback peripheralJobCallback;

  @Inject
  public RpcPeripheralJobDispatcherProviderProxy(PeripheralJobDispatcher peripheralJobDispatcher,
                                                 PeripheralJobCallback peripheralJobCallback) {
    this.peripheralJobDispatcher = peripheralJobDispatcher;
    this.peripheralJobCallback = peripheralJobCallback;
  }

  @Override
  public void initialize() {
    peripheralJobDispatcher.initialize();
  }

  @Override
  public boolean isInitialized() {
    return peripheralJobDispatcher.isInitialized();
  }

  @Override
  public void terminate() {
    peripheralJobDispatcher.terminate();
  }

  @Override
  public void dispatch() {
    peripheralJobDispatcher.dispatch();
  }

  @Override
  public void withdrawJob(@Nonnull Location location) throws IllegalArgumentException {
    peripheralJobDispatcher.withdrawJob(location);
  }

  @Override
  public void withdrawJob(@Nonnull PeripheralJob job) throws IllegalArgumentException {
    peripheralJobDispatcher.withdrawJob(job);
  }

  @Override
  public void peripheralJobFinished(@Nonnull PeripheralJob job) {
    peripheralJobCallback.peripheralJobFinished(job);
  }

  @Override
  public void peripheralJobFailed(@Nonnull PeripheralJob job) {
    peripheralJobCallback.peripheralJobFailed(job);
  }
}
