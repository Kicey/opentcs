package site.kicey.strategies.provider.scheduler;

import com.google.inject.multibindings.Multibinder;
import javax.inject.Singleton;
import org.opentcs.components.kernel.Scheduler.Module;
import org.opentcs.customizations.kernel.KernelInjectionModule;
import site.kicey.opentcs.strategies.rpc.proxy.DispatcherRpcProxy;
import site.kicey.opentcs.strategies.rpc.proxy.PeripheralJobDispatcherRpcProxy;
import site.kicey.opentcs.strategies.rpc.proxy.RouterRpcProxy;
import site.kicey.strategies.provider.scheduler.scheduling.DefaultScheduler;
import site.kicey.strategies.provider.scheduler.scheduling.ReservationPool;
import site.kicey.strategies.provider.scheduler.scheduling.modules.PausedVehicleModule;
import site.kicey.strategies.provider.scheduler.scheduling.modules.SameDirectionBlockModule;
import site.kicey.strategies.provider.scheduler.scheduling.modules.SingleVehicleBlockModule;

public class SchedulerProviderModule extends KernelInjectionModule {
  @Override
  protected void configure() {
    configureSchedulerDependencies();
    bindScheduler(DefaultScheduler.class);

    bindDispatcher(DispatcherRpcProxy.class);
    bindPeripheralJobDispatcher(PeripheralJobDispatcherRpcProxy.class);
    bindRouter(RouterRpcProxy.class);
  }

  private void configureSchedulerDependencies() {
    bind(ReservationPool.class).in(Singleton.class);

    Multibinder<Module> moduleBinder = schedulerModuleBinder();
    moduleBinder.addBinding().to(SingleVehicleBlockModule.class);
    moduleBinder.addBinding().to(SameDirectionBlockModule.class);
    moduleBinder.addBinding().to(PausedVehicleModule.class);
  }
}
