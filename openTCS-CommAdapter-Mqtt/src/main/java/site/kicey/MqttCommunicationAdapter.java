package site.kicey;

import java.util.concurrent.ScheduledExecutorService;
import javax.annotation.Nullable;
import org.opentcs.drivers.vehicle.BasicVehicleCommAdapter;
import org.opentcs.drivers.vehicle.MovementCommand;
import org.opentcs.drivers.vehicle.VehicleProcessModel;

public class MqttCommunicationAdapter extends BasicVehicleCommAdapter {

  public MqttCommunicationAdapter(VehicleProcessModel vehicleModel,
      int commandQueueCapacity, int sentQueueCapacity, String rechargeOperation,
      ScheduledExecutorService executor) {
    super(vehicleModel, commandQueueCapacity, sentQueueCapacity, rechargeOperation, executor);
  }

  @Override
  public void sendCommand(MovementCommand cmd) throws IllegalArgumentException {

  }

  @Override
  protected void connectVehicle() {

  }

  @Override
  protected void disconnectVehicle() {

  }

  @Override
  protected boolean isVehicleConnected() {
    return false;
  }

  @Override
  public void processMessage(@Nullable Object message) {

  }
}
