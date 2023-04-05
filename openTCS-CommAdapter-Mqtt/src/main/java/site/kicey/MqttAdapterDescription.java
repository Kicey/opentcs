package site.kicey;

import org.opentcs.drivers.vehicle.VehicleCommAdapterDescription;

public class MqttAdapterDescription
    extends VehicleCommAdapterDescription {

  /**
   * Creates a new instance.
   */
  public MqttAdapterDescription() {
  }

  @Override
  public String getDescription() {
    return "Mqtt Adapter";
  }

  @Override
  public boolean isSimVehicleCommAdapter() {
    return true;
  }

}
