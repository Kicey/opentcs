package site.kicey.strategies.rpc.dispatching;

import com.google.inject.Inject;
import javax.annotation.Nonnull;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.bootstrap.DubboBootstrap;
import org.opentcs.components.kernel.Dispatcher;
import org.opentcs.data.model.Vehicle;
import org.opentcs.data.order.ReroutingType;
import org.opentcs.data.order.TransportOrder;
import site.kicey.opentcs.strategies.rpc.api.RpcConstant;
import site.kicey.opentcs.strategies.rpc.api.RpcDispatcher;
import site.kicey.strategies.rpc.DubboConfiguration;

/**
 * Rpc dispatcher based on Dubbo.
 *
 * @author kicey
 */
@Slf4j
public class RpcDispatcherProxy implements Dispatcher {

  private final DubboConfiguration dubboConfiguration;

  private RpcDispatcher rpcDispatcher;

  @Inject
  public RpcDispatcherProxy(@Nonnull DubboConfiguration dubboConfiguration) {
    this.dubboConfiguration = dubboConfiguration;
  }

  /**
   * (Re-)Initializes this component before it is being used.
   */
  @Override
  public void initialize() {
    ReferenceConfig<RpcDispatcher> dispatcherServiceReferenceConfig = new ReferenceConfig<>();
    dispatcherServiceReferenceConfig.setInterface(RpcDispatcher.class);
    boolean dubboServiceStarted = false;
    int count = 0;
    while(!dubboServiceStarted) {
      try {
        Thread.sleep(1000);
        DubboBootstrap.getInstance()
            .application(RpcConstant.APPLICATION_NAME)
            .registry(new RegistryConfig(dubboConfiguration.zookeeperAddress()))
            .reference(dispatcherServiceReferenceConfig)
            .start();
        rpcDispatcher = dispatcherServiceReferenceConfig.get();
        dubboServiceStarted = true;
      } catch (Exception e) {
        ++count;
      }
    }
    log.info("Dispatcher Dubbo service started successfully, after {} count.", count);
    rpcDispatcher.initialize();
  }

  /**
   * Checks whether this component is initialized.
   *
   * @return <code>true</code> if, and only if, this component is initialized.
   */
  @Override
  public boolean isInitialized() {
    return rpcDispatcher.isInitialized();
  }

  /**
   * Terminates the instance and frees resources.
   */
  @Override
  public void terminate() {
    rpcDispatcher.terminate();
  }

  /**
   * Notifies the dispatcher that it should start the dispatching process.
   */
  @Override
  public void dispatch() {
    rpcDispatcher.dispatch();
  }

  /**
   * Notifies the dispatcher that the given transport order is to be withdrawn/aborted.
   *
   * @param order          The transport order to be withdrawn/aborted.
   * @param immediateAbort Whether the order should be aborted immediately instead of withdrawn.
   */
  @Override
  public void withdrawOrder(@Nonnull TransportOrder order, boolean immediateAbort) {
    rpcDispatcher.withdrawOrder(order, immediateAbort);
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
    rpcDispatcher.withdrawOrder(vehicle, immediateAbort);
  }

  /**
   * Notifies the dispatcher of changes in the topology.
   */
  @Override
  public void topologyChanged() {
    rpcDispatcher.topologyChanged();
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
    rpcDispatcher.reroute(vehicle, reroutingType);
  }
}
