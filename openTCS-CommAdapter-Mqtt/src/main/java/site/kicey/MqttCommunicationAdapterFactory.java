package site.kicey;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import org.opentcs.data.model.Vehicle;
import org.opentcs.drivers.vehicle.VehicleCommAdapter;
import org.opentcs.drivers.vehicle.VehicleCommAdapterDescription;
import org.opentcs.drivers.vehicle.VehicleCommAdapterFactory;

import static java.util.Objects.requireNonNull;

public class MqttCommunicationAdapterFactory implements VehicleCommAdapterFactory {
  private final MqttAdapterComponentsFactory adapterFactory;
  @Inject
  public MqttCommunicationAdapterFactory(MqttAdapterComponentsFactory componentsFactory) {
    this.adapterFactory = requireNonNull(componentsFactory, "componentsFactory");
  }

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
  public VehicleCommAdapterDescription getDescription() {
    return new MqttAdapterDescription();
  }

  @Override
  public boolean providesAdapterFor(@Nonnull Vehicle vehicle) {
    return true;
  }

  @Nullable
  @Override
  public VehicleCommAdapter getAdapterFor(@Nonnull Vehicle vehicle) {
    requireNonNull(vehicle, "vehicle");
    return adapterFactory.createMqttCommAdapter(vehicle);
  }
}
