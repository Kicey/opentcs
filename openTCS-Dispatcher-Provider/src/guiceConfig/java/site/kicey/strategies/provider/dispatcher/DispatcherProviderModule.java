package site.kicey.strategies.provider.dispatcher;

import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.MapBinder;
import com.google.inject.multibindings.Multibinder;
import java.util.Comparator;
import javax.inject.Singleton;
import org.opentcs.customizations.kernel.KernelInjectionModule;
import org.opentcs.data.model.Vehicle;
import org.opentcs.data.order.ReroutingType;
import org.opentcs.data.order.TransportOrder;
import site.kicey.strategies.provider.dispatcher.dispatching.AssignmentCandidate;
import site.kicey.strategies.provider.dispatcher.dispatching.DefaultDispatcher;
import site.kicey.strategies.provider.dispatcher.dispatching.DefaultDispatcherConfiguration;
import site.kicey.strategies.provider.dispatcher.dispatching.OrderReservationPool;
import site.kicey.strategies.provider.dispatcher.dispatching.RerouteUtil;
import site.kicey.strategies.provider.dispatcher.dispatching.TransportOrderUtil;
import site.kicey.strategies.provider.dispatcher.dispatching.phase.parking.DefaultParkingPositionSupplier;
import site.kicey.strategies.provider.dispatcher.dispatching.phase.parking.ParkingPositionSupplier;
import site.kicey.strategies.provider.dispatcher.dispatching.phase.recharging.DefaultRechargePositionSupplier;
import site.kicey.strategies.provider.dispatcher.dispatching.phase.recharging.RechargePositionSupplier;
import site.kicey.strategies.provider.dispatcher.dispatching.priorization.CompositeOrderCandidateComparator;
import site.kicey.strategies.provider.dispatcher.dispatching.priorization.CompositeOrderComparator;
import site.kicey.strategies.provider.dispatcher.dispatching.priorization.CompositeVehicleCandidateComparator;
import site.kicey.strategies.provider.dispatcher.dispatching.priorization.CompositeVehicleComparator;
import site.kicey.strategies.provider.dispatcher.dispatching.priorization.candidate.CandidateComparatorByCompleteRoutingCosts;
import site.kicey.strategies.provider.dispatcher.dispatching.priorization.candidate.CandidateComparatorByDeadline;
import site.kicey.strategies.provider.dispatcher.dispatching.priorization.candidate.CandidateComparatorByEnergyLevel;
import site.kicey.strategies.provider.dispatcher.dispatching.priorization.candidate.CandidateComparatorByInitialRoutingCosts;
import site.kicey.strategies.provider.dispatcher.dispatching.priorization.candidate.CandidateComparatorByOrderAge;
import site.kicey.strategies.provider.dispatcher.dispatching.priorization.candidate.CandidateComparatorByOrderName;
import site.kicey.strategies.provider.dispatcher.dispatching.priorization.candidate.CandidateComparatorByVehicleName;
import site.kicey.strategies.provider.dispatcher.dispatching.priorization.candidate.CandidateComparatorIdleFirst;
import site.kicey.strategies.provider.dispatcher.dispatching.priorization.transportorder.TransportOrderComparatorByAge;
import site.kicey.strategies.provider.dispatcher.dispatching.priorization.transportorder.TransportOrderComparatorByDeadline;
import site.kicey.strategies.provider.dispatcher.dispatching.priorization.transportorder.TransportOrderComparatorByName;
import site.kicey.strategies.provider.dispatcher.dispatching.priorization.vehicle.VehicleComparatorByEnergyLevel;
import site.kicey.strategies.provider.dispatcher.dispatching.priorization.vehicle.VehicleComparatorByName;
import site.kicey.strategies.provider.dispatcher.dispatching.priorization.vehicle.VehicleComparatorIdleFirst;
import site.kicey.strategies.provider.dispatcher.dispatching.rerouting.ForcedReroutingStrategy;
import site.kicey.strategies.provider.dispatcher.dispatching.rerouting.RegularDriveOrderMerger;
import site.kicey.strategies.provider.dispatcher.dispatching.rerouting.RegularReroutingStrategy;
import site.kicey.strategies.provider.dispatcher.dispatching.rerouting.ReroutingStrategy;
import site.kicey.strategies.provider.dispatcher.dispatching.selection.AssignmentCandidateSelectionFilter;
import site.kicey.strategies.provider.dispatcher.dispatching.selection.ParkVehicleSelectionFilter;
import site.kicey.strategies.provider.dispatcher.dispatching.selection.RechargeVehicleSelectionFilter;
import site.kicey.strategies.provider.dispatcher.dispatching.selection.ReparkVehicleSelectionFilter;
import site.kicey.strategies.provider.dispatcher.dispatching.selection.TransportOrderSelectionFilter;
import site.kicey.strategies.provider.dispatcher.dispatching.selection.VehicleSelectionFilter;
import site.kicey.strategies.provider.dispatcher.dispatching.selection.candidates.CompositeAssignmentCandidateSelectionFilter;
import site.kicey.strategies.provider.dispatcher.dispatching.selection.candidates.IsProcessable;
import site.kicey.strategies.provider.dispatcher.dispatching.selection.orders.CompositeTransportOrderSelectionFilter;
import site.kicey.strategies.provider.dispatcher.dispatching.selection.orders.ContainsLockedTargetLocations;
import site.kicey.strategies.provider.dispatcher.dispatching.selection.vehicles.CompositeParkVehicleSelectionFilter;
import site.kicey.strategies.provider.dispatcher.dispatching.selection.vehicles.CompositeRechargeVehicleSelectionFilter;
import site.kicey.strategies.provider.dispatcher.dispatching.selection.vehicles.CompositeReparkVehicleSelectionFilter;
import site.kicey.strategies.provider.dispatcher.dispatching.selection.vehicles.CompositeVehicleSelectionFilter;
import site.kicey.strategies.provider.dispatcher.dispatching.selection.vehicles.IsIdleAndDegraded;
import site.kicey.strategies.provider.dispatcher.dispatching.selection.vehicles.IsParkable;
import site.kicey.strategies.provider.dispatcher.dispatching.selection.vehicles.IsReparkable;

public class DispatcherProviderModule extends KernelInjectionModule {

  @Override
  protected void configure() {
    configureDispatcherDependencies();
    bindDispatcher(DefaultDispatcher.class);
  }

  private void configureDispatcherDependencies() {
    Multibinder.newSetBinder(binder(), VehicleSelectionFilter.class);
    Multibinder.newSetBinder(binder(), TransportOrderSelectionFilter.class)
        .addBinding().to(ContainsLockedTargetLocations.class);
    Multibinder.newSetBinder(binder(), ParkVehicleSelectionFilter.class)
        .addBinding().to(IsParkable.class);
    Multibinder.newSetBinder(binder(), ReparkVehicleSelectionFilter.class)
        .addBinding().to(IsReparkable.class);
    Multibinder.newSetBinder(binder(), RechargeVehicleSelectionFilter.class)
        .addBinding().to(IsIdleAndDegraded.class);
    Multibinder.newSetBinder(binder(), AssignmentCandidateSelectionFilter.class)
        .addBinding().to(IsProcessable.class);

    bind(CompositeParkVehicleSelectionFilter.class)
        .in(Singleton.class);
    bind(CompositeReparkVehicleSelectionFilter.class)
        .in(Singleton.class);
    bind(CompositeRechargeVehicleSelectionFilter.class)
        .in(Singleton.class);
    bind(CompositeTransportOrderSelectionFilter.class)
        .in(Singleton.class);
    bind(CompositeVehicleSelectionFilter.class)
        .in(Singleton.class);
    bind(CompositeAssignmentCandidateSelectionFilter.class)
        .in(Singleton.class);

    bind(DefaultDispatcherConfiguration.class)
        .toInstance(getConfigBindingProvider().get(DefaultDispatcherConfiguration.PREFIX,
            DefaultDispatcherConfiguration.class));

    bind(OrderReservationPool.class)
        .in(Singleton.class);

    bind(ParkingPositionSupplier.class)
        .to(DefaultParkingPositionSupplier.class)
        .in(Singleton.class);
    bind(RechargePositionSupplier.class)
        .to(DefaultRechargePositionSupplier.class)
        .in(Singleton.class);

    MapBinder<String, Comparator<Vehicle>> vehicleComparatorBinder
        = MapBinder.newMapBinder(binder(),
        new TypeLiteral<String>() {
        },
        new TypeLiteral<Comparator<Vehicle>>() {
        });
    vehicleComparatorBinder
        .addBinding(VehicleComparatorByEnergyLevel.CONFIGURATION_KEY)
        .to(VehicleComparatorByEnergyLevel.class);
    vehicleComparatorBinder
        .addBinding(VehicleComparatorByName.CONFIGURATION_KEY)
        .to(VehicleComparatorByName.class);
    vehicleComparatorBinder
        .addBinding(VehicleComparatorIdleFirst.CONFIGURATION_KEY)
        .to(VehicleComparatorIdleFirst.class);

    MapBinder<String, Comparator<TransportOrder>> orderComparatorBinder
        = MapBinder.newMapBinder(binder(),
        new TypeLiteral<String>() {
        },
        new TypeLiteral<Comparator<TransportOrder>>() {
        });
    orderComparatorBinder
        .addBinding(TransportOrderComparatorByAge.CONFIGURATION_KEY)
        .to(TransportOrderComparatorByAge.class);
    orderComparatorBinder
        .addBinding(TransportOrderComparatorByDeadline.CONFIGURATION_KEY)
        .to(TransportOrderComparatorByDeadline.class);
    orderComparatorBinder
        .addBinding(TransportOrderComparatorByName.CONFIGURATION_KEY)
        .to(TransportOrderComparatorByName.class);

    MapBinder<String, Comparator<AssignmentCandidate>> candidateComparatorBinder
        = MapBinder.newMapBinder(binder(),
        new TypeLiteral<String>() {
        },
        new TypeLiteral<Comparator<AssignmentCandidate>>() {
        });
    candidateComparatorBinder
        .addBinding(CandidateComparatorByCompleteRoutingCosts.CONFIGURATION_KEY)
        .to(CandidateComparatorByCompleteRoutingCosts.class);
    candidateComparatorBinder
        .addBinding(CandidateComparatorByDeadline.CONFIGURATION_KEY)
        .to(CandidateComparatorByDeadline.class);
    candidateComparatorBinder
        .addBinding(CandidateComparatorByEnergyLevel.CONFIGURATION_KEY)
        .to(CandidateComparatorByEnergyLevel.class);
    candidateComparatorBinder
        .addBinding(CandidateComparatorByInitialRoutingCosts.CONFIGURATION_KEY)
        .to(CandidateComparatorByInitialRoutingCosts.class);
    candidateComparatorBinder
        .addBinding(CandidateComparatorByOrderAge.CONFIGURATION_KEY)
        .to(CandidateComparatorByOrderAge.class);
    candidateComparatorBinder
        .addBinding(CandidateComparatorByOrderName.CONFIGURATION_KEY)
        .to(CandidateComparatorByOrderName.class);
    candidateComparatorBinder
        .addBinding(CandidateComparatorByVehicleName.CONFIGURATION_KEY)
        .to(CandidateComparatorByVehicleName.class);
    candidateComparatorBinder
        .addBinding(CandidateComparatorIdleFirst.CONFIGURATION_KEY)
        .to(CandidateComparatorIdleFirst.class);

    bind(CompositeVehicleComparator.class)
        .in(Singleton.class);
    bind(CompositeOrderComparator.class)
        .in(Singleton.class);
    bind(CompositeOrderCandidateComparator.class)
        .in(Singleton.class);
    bind(CompositeVehicleCandidateComparator.class)
        .in(Singleton.class);

    bind(TransportOrderUtil.class)
        .in(Singleton.class);

    configureRerouteComponents();
  }

  private void configureRerouteComponents() {
    bind(RerouteUtil.class).in(Singleton.class);
    bind(RegularReroutingStrategy.class).in(Singleton.class);
    bind(RegularDriveOrderMerger.class).in(Singleton.class);

    MapBinder<ReroutingType, ReroutingStrategy> reroutingStrategies
        = MapBinder.newMapBinder(binder(),
        ReroutingType.class,
        ReroutingStrategy.class);
    reroutingStrategies
        .addBinding(ReroutingType.REGULAR)
        .to(RegularReroutingStrategy.class);
    reroutingStrategies
        .addBinding(ReroutingType.FORCED)
        .to(ForcedReroutingStrategy.class);
  }
}
