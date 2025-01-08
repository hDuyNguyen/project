package com.example.powertrackingapp.controller;

import android.util.Log;

import static com.example.powertrackingapp.AppConstant.TAG;

import com.example.powertrackingapp.model.Alert;
import com.example.powertrackingapp.model.UpdateUserInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.nio.channels.InterruptedByTimeoutException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Repository {

    private static volatile Repository instance;
    private static IMqttClient client = null;
    private String payload;
    private final boolean[] responseReceived = {false};

    private Repository() {
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

    public String getInfoUser(String username, String password, String deviceId) throws MqttException, InterruptedException {
        connectToServer();

        String requestTopic = "login/client";
        String topicSubscribe = "login/client/" + deviceId;
        String requestMessage = String.format(
                "{ \"username\": \"%s\", \"password\": \"%s\", \"deviceId\": \"%s\" }",
                username, password, deviceId
        );

        handleCallBack();

        client.publish(requestTopic, new MqttMessage(requestMessage.getBytes()));
        Log.i(TAG, "Send request to: " + requestTopic);

        client.subscribe(topicSubscribe);
        Log.i(TAG, "Subscribe to topic: " + topicSubscribe);

        // Vòng lặp chờ phản hồi trong 2 giây
        long startTime = System.currentTimeMillis();
        while (!responseReceived[0] && (System.currentTimeMillis() - startTime) < 2000) {
            Thread.sleep(100); // Chờ 100ms trước khi kiểm tra lại
        }

        return payload;
    }

    private void handleCallBack() {
        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                Log.i(TAG, "connectionLost: " + cause);
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                payload = new String(message.getPayload());
                responseReceived[0] = true;
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

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

    public String getHistory(Alert alert, String deviceId) throws Exception {
        connectToServer();

        String requestTopic = "history/client";
        client.subscribe(requestTopic);

        String topicSubscribe = "history/client/" + deviceId;

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        MqttMessage message = new MqttMessage();
        message.setPayload(objectMapper.writeValueAsBytes(alert));

        client.publish(requestTopic, message);
        Log.i(TAG, "Send request to: " + requestTopic);

        client.subscribe(topicSubscribe);
        Log.i(TAG, "Subscribe to topic: " + topicSubscribe);

        handleCallBack();

        // Vòng lặp chờ phản hồi trong 2 giây
        long startTime = System.currentTimeMillis();
        while (!responseReceived[0] && (System.currentTimeMillis() - startTime) < 2000) {
            Thread.sleep(100); // Chờ 100ms trước khi kiểm tra lại
        }

        return payload;
    }

    public String editUserInfo(UpdateUserInfo updateUserInfo, String deviceId) throws Exception {
        connectToServer();

        String requestTopic = "edit-user-info/client";
        client.subscribe(requestTopic);

        String topicSubscribe = "edit-user-info/client/" + deviceId;

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        MqttMessage message = new MqttMessage();
        message.setPayload(objectMapper.writeValueAsBytes(updateUserInfo));

        client.publish(requestTopic, message);
        Log.i(TAG, "Send request to: " + requestTopic);

        client.subscribe(topicSubscribe);
        Log.i(TAG, "Subscribe to topic: " + topicSubscribe);

        handleCallBack();

        // Vòng lặp chờ phản hồi trong 2 giây
        long startTime = System.currentTimeMillis();
        while (!responseReceived[0] && (System.currentTimeMillis() - startTime) < 2000) {
            Thread.sleep(100); // Chờ 100ms trước khi kiểm tra lại
        }

        return payload;
    }

}
