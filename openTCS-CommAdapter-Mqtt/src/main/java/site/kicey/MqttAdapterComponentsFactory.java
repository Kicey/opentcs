package site.kicey;

import org.opentcs.data.model.Vehicle;

public interface MqttAdapterComponentsFactory {
  MqttCommunicationAdapter createMqttCommAdapter(Vehicle vehicle);
}
