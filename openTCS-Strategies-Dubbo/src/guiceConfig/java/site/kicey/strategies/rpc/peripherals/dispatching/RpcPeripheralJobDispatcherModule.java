package site.kicey.strategies.rpc.peripherals.dispatching;

import org.opentcs.customizations.kernel.KernelInjectionModule;

public class RpcPeripheralJobDispatcherModule extends KernelInjectionModule {

    @Override
    protected void configure() {
      bindPeripheralJobDispatcher(RpcPeripheralJobDispatcher.class);
    }
}
