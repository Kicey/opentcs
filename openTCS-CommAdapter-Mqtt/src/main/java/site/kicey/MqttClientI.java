package site.kicey;

import org.eclipse.paho.mqttv5.client.MqttConnectionOptions;
import org.eclipse.paho.mqttv5.client.persist.MemoryPersistence;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.eclipse.paho.mqttv5.client.MqttClient;

public class MqttClientI {
  private MqttClient mqttClient;
  public MqttClientI(String broker,String userName, String password){
    String clientId = "mqttx_cbc693a9_+";
    MemoryPersistence persistence = new MemoryPersistence();
    try {
      this.mqttClient = new MqttClient(broker, clientId, persistence);
      MqttConnectionOptions connOpts = new MqttConnectionOptions();
      connOpts.setCleanStart(true);
      connOpts.setUserName(userName);
      connOpts.setPassword(password.getBytes());
      this.mqttClient.connect(connOpts);


    } catch (MqttException me) {
      me.printStackTrace();
    }
  }
  public void publish(String topic,String content,int qos) {
    MqttMessage message = new MqttMessage(content.getBytes());
    message.setQos(qos);
    try {
      this.mqttClient.publish(topic, message);
    } catch (MqttException me) {
      System.out.println("reason " + me.getReasonCode());
      System.out.println("msg " + me.getMessage());
      System.out.println("loc " + me.getLocalizedMessage());
      System.out.println("cause " + me.getCause());
      System.out.println("excep " + me);
      me.printStackTrace();
    }
  }
  public void close()  {
    try {
      this.mqttClient.disconnect();
      this.mqttClient.close();
    } catch (MqttException e) {
      e.printStackTrace();
    }

  }

}
