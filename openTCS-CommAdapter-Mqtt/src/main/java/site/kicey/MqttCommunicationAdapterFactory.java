package site.kicey;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.opentcs.data.model.Vehicle;
import org.opentcs.drivers.vehicle.VehicleCommAdapter;
import org.opentcs.drivers.vehicle.VehicleCommAdapterDescription;
import org.opentcs.drivers.vehicle.VehicleCommAdapterFactory;

public class MqttCommunicationAdapterFactory implements VehicleCommAdapterFactory {

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
    return null;
  }

  @Override
  public boolean providesAdapterFor(@Nonnull Vehicle vehicle) {
    return false;
  }

  @Nullable
  @Override
  public VehicleCommAdapter getAdapterFor(@Nonnull Vehicle vehicle) {
    return null;
  }
}
