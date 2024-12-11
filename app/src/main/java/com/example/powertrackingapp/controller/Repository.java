package com.example.powertrackingapp.controller;

import android.util.Log;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Repository {

    private static final String TAG = "duynm";

    private static volatile Repository instance;
    private IMqttClient client = null;
    private ExecutorService executorService;
    private String payload;

    private Repository() {
        executorService = Executors.newSingleThreadExecutor();
    }

    public static Repository getInstance() {
        if (instance == null) {
            synchronized (Repository.class) {
                if (instance == null) {
                    instance = new Repository();
                }
            }
        }
        return instance;
    }

    public void connectToServer() {

            try {
                String broker = "ssl://i1731e41.ala.asia-southeast1.emqxsl.com:8883";
                String clientId = "duynm";
                String username = "springboot";
                String password = "12345678";

                client = new MqttClient(broker, clientId, new MemoryPersistence());
                MqttConnectOptions connOpts = new MqttConnectOptions();
                connOpts.setCleanSession(true); // Không lưu lại trạng thái cũ
                connOpts.setUserName(username);
                connOpts.setPassword(password.toCharArray());
                connOpts.setAutomaticReconnect(true); // Tự động kết nối lại nếu mất
                connOpts.setConnectionTimeout(10); // Timeout sau 10 giây

                client.connect(connOpts);
                Log.i(TAG, "Kết nối thành công tới broker!");
            } catch (MqttException e) {
                Log.e(TAG, "Lỗi kết nối MQTT: ", e);
            }
    }

    public String getInfoUser(String username, String password, String deviceId) throws MqttException {
        String requestTopic = "login/client";
        String requestMessage = String.format(
                "{ \"username\": \"%s\", \"password\": \"%s\", \"deviceId\": \"%s\" }",
                username, password, deviceId
        );

        client.publish(requestTopic, new MqttMessage(requestMessage.getBytes()));
        Log.i(TAG, "Send request to: " + requestTopic);

        String topicSubscribe = "login/client/" + deviceId;
        client.subscribe(topicSubscribe);
        Log.i(TAG, "Subscribe to topic: " + topicSubscribe);

        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                Log.i(TAG, "connectionLost: " + cause);
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                payload = new String(message.getPayload());
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });

        return payload;
    }


    public void disconnect() {
        try {
            if (client != null && client.isConnected()) {
                client.disconnect();
                Log.i("duynm", "Đã ngắt kết nối từ broker.");
            }
        } catch (MqttException e) {
            Log.e("duynm", "Lỗi khi ngắt kết nối: ", e);
        }
    }

    public boolean isConnected() {
        return client != null && client.isConnected();
    }
}
