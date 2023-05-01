package site.kicey.strategies.rpc.peripherals.dispatching;

import javax.annotation.Nonnull;
import org.opentcs.components.kernel.PeripheralJobDispatcher;
import org.opentcs.data.model.Location;
import org.opentcs.data.peripherals.PeripheralJob;
import org.opentcs.drivers.peripherals.PeripheralJobCallback;

public class RpcPeripheralJobDispatcher implements PeripheralJobDispatcher, PeripheralJobCallback {

  /**
   *
   */
  @Override
  public void initialize() {

  }

  /**
   * @return
   */
  @Override
  public boolean isInitialized() {
    return false;
  }

  /**
   *
   */
  @Override
  public void terminate() {

  }

  /**
   *
   */
  @Override
  public void dispatch() {

  }

  /**
   * @param location The location representing a peripheral device whose job is withdrawn.
   * @throws IllegalArgumentException
   */
  @Override
  public void withdrawJob(@Nonnull Location location) throws IllegalArgumentException {

  }

  /**
   * @param job The job to be withdrawn.
   * @throws IllegalArgumentException
   */
  @Override
  public void withdrawJob(@Nonnull PeripheralJob job) throws IllegalArgumentException {

  }

  /**
   * @param job The job that was successfully completed.
   */
  @Override
  public void peripheralJobFinished(@Nonnull PeripheralJob job) {

  }

  /**
   * @param job The job whose completion has failed.
   */
  @Override
  public void peripheralJobFailed(@Nonnull PeripheralJob job) {

  }
}
