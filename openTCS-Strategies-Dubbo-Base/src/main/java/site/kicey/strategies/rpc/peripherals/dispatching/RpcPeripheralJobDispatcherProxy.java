package site.kicey.strategies.rpc.peripherals.dispatching;

import com.google.inject.Inject;
import javax.annotation.Nonnull;
import org.opentcs.components.kernel.PeripheralJobDispatcher;
import org.opentcs.data.model.Location;
import org.opentcs.data.peripherals.PeripheralJob;
import org.opentcs.drivers.peripherals.PeripheralJobCallback;
import site.kicey.opentcs.strategies.rpc.api.RpcPeripheralJobDispatcher;

public class RpcPeripheralJobDispatcherProxy implements PeripheralJobDispatcher, PeripheralJobCallback {

  private final RpcPeripheralJobDispatcher rpcPeripheralJobDispatcher;

  @Inject
  public RpcPeripheralJobDispatcherProxy(RpcPeripheralJobDispatcher rpcPeripheralJobDispatcher) {
    this.rpcPeripheralJobDispatcher = rpcPeripheralJobDispatcher;
  }

  /**
   *
   */
  @Override
  public void initialize() {
    rpcPeripheralJobDispatcher.initialize();
  }

  /**
   * @return
   */
  @Override
  public boolean isInitialized() {
    return rpcPeripheralJobDispatcher.isInitialized();
  }

  /**
   *
   */
  @Override
  public void terminate() {
    rpcPeripheralJobDispatcher.terminate();
  }

  /**
   *
   */
  @Override
  public void dispatch() {
    rpcPeripheralJobDispatcher.dispatch();
  }

  /**
   * @param location The location representing a peripheral device whose job is withdrawn.
   * @throws IllegalArgumentException
   */
  @Override
  public void withdrawJob(@Nonnull Location location) throws IllegalArgumentException {
    rpcPeripheralJobDispatcher.withdrawJob(location);
  }

  /**
   * @param job The job to be withdrawn.
   * @throws IllegalArgumentException
   */
  @Override
  public void withdrawJob(@Nonnull PeripheralJob job) throws IllegalArgumentException {
    rpcPeripheralJobDispatcher.withdrawJob(job);
  }

  /**
   * @param job The job that was successfully completed.
   */
  @Override
  public void peripheralJobFinished(@Nonnull PeripheralJob job) {
    rpcPeripheralJobDispatcher.peripheralJobFinished(job);
  }

  /**
   * @param job The job whose completion has failed.
   */
  @Override
  public void peripheralJobFailed(@Nonnull PeripheralJob job) {
    rpcPeripheralJobDispatcher.peripheralJobFailed(job);
  }
}
