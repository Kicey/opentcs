/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package site.kicey.strategies.provider.dispatcher.dispatching.selection.vehicles;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import javax.inject.Inject;
import org.opentcs.data.model.Vehicle;
import site.kicey.strategies.provider.dispatcher.dispatching.selection.RechargeVehicleSelectionFilter;

/**
 * A collection of {@link RechargeVehicleSelectionFilter}s.
 *
 * @author Martin Grzenia (Fraunhofer IML)
 */
public class CompositeRechargeVehicleSelectionFilter
    implements RechargeVehicleSelectionFilter {

  /**
   * The {@link RechargeVehicleSelectionFilter}s.
   */
  private final Set<RechargeVehicleSelectionFilter> filters;

  @Inject
  public CompositeRechargeVehicleSelectionFilter(Set<RechargeVehicleSelectionFilter> filters) {
    this.filters = requireNonNull(filters, "filters");
  }

  @Override
  public Collection<String> apply(Vehicle vehicle) {
    return filters.stream()
        .flatMap(filter -> filter.apply(vehicle).stream())
        .collect(Collectors.toList());
  }
}
