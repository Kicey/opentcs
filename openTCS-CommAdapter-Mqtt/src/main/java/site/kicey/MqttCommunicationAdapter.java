package site.kicey;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import javax.annotation.Nullable;
import javax.inject.Inject;

import com.google.inject.assistedinject.Assisted;
import org.opentcs.customizations.kernel.KernelExecutor;
import org.opentcs.data.model.Vehicle;
import org.opentcs.drivers.vehicle.BasicVehicleCommAdapter;
import org.opentcs.drivers.vehicle.MovementCommand;
import org.opentcs.drivers.vehicle.SimVehicleCommAdapter;
import org.opentcs.drivers.vehicle.VehicleProcessModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MqttCommunicationAdapter extends BasicVehicleCommAdapter implements SimVehicleCommAdapter {
  private static final Logger LOG = LoggerFactory.getLogger(MqttCommunicationAdapter.class);

  private MqttClientI mqttClientI;

  @Inject
  public MqttCommunicationAdapter(@Assisted Vehicle vehicle,
                                  @KernelExecutor ScheduledExecutorService kernelExecutor) {
    super(new VehicleProcessModel(vehicle),
        2,
        1,
        "CHARGE",
        kernelExecutor);
  }
  @Override
  public void initialize() {
    LOG.info("MQTT INIT");
    super.initialize();
    this.mqttClientI = new MqttClientI("tcp://kicey.site:1883","test","test");
  }

  @Override
  public void sendCommand(MovementCommand cmd) throws IllegalArgumentException {
    LOG.info("MOVE "+cmd);
    String topic = "opentcs/command";
    String operation = cmd.getOperation();
    String route = cmd.getRoute().toString();
    String destination = cmd.getFinalDestination().toString();
    this.mqttClientI.publish(topic, operation+"\n"+route+"\n"+destination,0);
  }


  @Override
  protected void connectVehicle() {

  }
  @Override
  public synchronized void enable() {
    super.enable();
    this.mqttClientI.publish("opentcs/enable","Enable",0);
  }

  @Override
  protected void disconnectVehicle() {

  }

  @Override
  public synchronized void disable() {
    super.disable();
    if(this.mqttClientI != null){
      this.mqttClientI.publish("opentcs/enable","Disable",0);
    }
  }

  @Override
  protected boolean isVehicleConnected() {
    return true;
  }

  @Override
  public void processMessage(@Nullable Object message) {
    LOG.info("MESSAGE" + message);
  }

  @Override
  public void initVehiclePosition(@Nullable String newPos) {
    ((ExecutorService) getExecutor()).submit(() -> {
      getProcessModel().setVehiclePosition(newPos);
      this.mqttClientI.publish("opentcs/position",newPos,0);
    });

  }

  @Override
  public VehicleProcessModel getProcessModel() {
    return super.getProcessModel();
  }
}
