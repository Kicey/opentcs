package site.kicey.strategies.rpc.peripherals.dispatching;

import com.google.inject.Inject;

import javax.annotation.Nonnull;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.bootstrap.DubboBootstrap;
import org.opentcs.components.kernel.PeripheralJobDispatcher;
import org.opentcs.data.model.Location;
import org.opentcs.data.peripherals.PeripheralJob;
import org.opentcs.drivers.peripherals.PeripheralJobCallback;
import site.kicey.opentcs.strategies.rpc.api.RpcConstant;
import site.kicey.opentcs.strategies.rpc.api.RpcPeripheralJobDispatcher;
import site.kicey.strategies.rpc.DubboConfiguration;

@Slf4j
public class RpcPeripheralJobDispatcherProxy implements PeripheralJobDispatcher, PeripheralJobCallback {

  private final DubboConfiguration dubboConfiguration;

  private RpcPeripheralJobDispatcher rpcPeripheralJobDispatcher;

  @Inject
  public RpcPeripheralJobDispatcherProxy(DubboConfiguration dubboConfiguration) {
    this.dubboConfiguration = dubboConfiguration;
  }

  /**
   *
   */
  @Override
  public void initialize() {

    ReferenceConfig<RpcPeripheralJobDispatcher> dispatcherServiceReferenceConfig = new ReferenceConfig<>();
    dispatcherServiceReferenceConfig.setInterface(RpcPeripheralJobDispatcher.class);
    boolean dubboServiceStarted = false;
    int count = 0;
    while (!dubboServiceStarted) {
      try {
        Thread.sleep(1000);
        DubboBootstrap.getInstance()
            .application(RpcConstant.APPLICATION_NAME)
            .registry(new RegistryConfig(dubboConfiguration.zookeeperAddress()))
            .reference(dispatcherServiceReferenceConfig)
            .start();
        rpcPeripheralJobDispatcher = dispatcherServiceReferenceConfig.get();
        dubboServiceStarted = true;
      } catch (Exception exception) {
        ++count;
      }
    }

    log.info("PeripheralJobDispatcher Dubbo service started successfully, after {} count.", count);
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
