package com.example.powertrackingapp.controller;

import android.util.Log;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Repository {

    private static volatile Repository instance;
    private IMqttClient client;
    private ExecutorService executorService;

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
        executorService.execute(() -> {
            try {
                String broker = "ssl://i1731e41.ala.asia-southeast1.emqxsl.com:8883";
                String clientId = MqttClient.generateClientId();
                String username = "springboot";
                String password = "12345678";

                client = new MqttClient(broker, clientId);
                MqttConnectOptions options = new MqttConnectOptions();
                options.setUserName(username);
                options.setPassword(password.toCharArray());
                options.setAutomaticReconnect(true);
                options.setCleanSession(true);
                options.setConnectionTimeout(10);

                client.connect(options);
                Log.i("duynm", "Kết nối thành công tới broker!");
            } catch (MqttException e) {
                Log.e("duynm", "Lỗi kết nối MQTT: ", e);
            }
        });
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
