import org.eclipse.paho.mqttv5.client.MqttClient;
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions;
import org.eclipse.paho.mqttv5.client.persist.MemoryPersistence;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.junit.jupiter.api.Test;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
public class MqttTest {
  String topic = "testtopic";
  String content = "hello";
  int qos = 0;
  String broker = "wss://kicey.site:8084";
  String userName = "test";
  String password = "test";
  String clientId = "mqttx_cbc693a9";
  public class MyHostnameVerifier implements HostnameVerifier {
    @Override
    public boolean verify(String hostname, SSLSession session) {
      return true;
    }
  }
  @Test
  public void Main(){
    MemoryPersistence persistence = new MemoryPersistence();
    try {
      MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
      MqttConnectionOptions connOpts = new MqttConnectionOptions();
      connOpts.setCleanStart(true);
      connOpts.setSSLHostnameVerifier(new MyHostnameVerifier());
      connOpts.setUserName(userName);
      connOpts.setPassword(password.getBytes());
      sampleClient.connect(connOpts);
      MqttMessage message = new MqttMessage(content.getBytes());
      message.setQos(qos);
      sampleClient.publish(topic, message);
      sampleClient.disconnect();
      sampleClient.close();
    } catch (MqttException me) {
      System.out.println("reason " + me.getReasonCode());
      System.out.println("msg " + me.getMessage());
      System.out.println("loc " + me.getLocalizedMessage());
      System.out.println("cause " + me.getCause());
      System.out.println("excep " + me);
      me.printStackTrace();
    }
  }
}
