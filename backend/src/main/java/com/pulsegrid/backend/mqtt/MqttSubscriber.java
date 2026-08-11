package com.pulsegrid.backend.mqtt;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class MqttSubscriber implements ApplicationRunner {

    private static final String BROKER_URL = "tcp://localhost:1883";
    private static final String CLIENT_ID = "pulsegrid-backend";
    private static final String TOPIC = "sensors/temperature";

    @Override
    public void run(ApplicationArguments args) throws Exception {
        MqttClient client = new MqttClient(BROKER_URL, CLIENT_ID);

        client.setCallback(new MqttCallback() {

            @Override
            public void connectionLost(Throwable cause) {
                System.out.println(
                        "MQTT connection lost: " + cause.getMessage()
                );
            }

            @Override
            public void messageArrived(
                    String topic,
                    MqttMessage message
            ) {
                String payload = new String(message.getPayload());

                System.out.println("MQTT topic: " + topic);
                System.out.println("MQTT payload: " + payload);
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
            }
        });

        client.connect();
        client.subscribe(TOPIC);

        System.out.println("Connected to MQTT broker: " + BROKER_URL);
        System.out.println("Subscribed to topic: " + TOPIC);
    }
}