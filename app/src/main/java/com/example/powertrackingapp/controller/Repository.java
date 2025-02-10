package com.example.powertrackingapp.controller;

import static com.example.powertrackingapp.AppConstant.TAG;
import static com.example.powertrackingapp.AppConstant.TIME_OUT;

import android.util.Log;

import com.example.powertrackingapp.model.Alert;
import com.example.powertrackingapp.model.CreateUserRequest;
import com.example.powertrackingapp.model.EditPasswordRequest;
import com.example.powertrackingapp.model.PowerConsumption;
import com.example.powertrackingapp.model.UpdateUserInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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
            String username = "dungdung";
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

    public boolean isConnected() {
        return client != null && client.isConnected();
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

    public void sendTokenToServer(String topic, String message) {
        try {
            if (client != null && client.isConnected()) {
                MqttMessage mqttMessage = new MqttMessage(message.getBytes());
                mqttMessage.setQos(1);
                client.publish(topic, mqttMessage);
                Log.d(TAG, "Gửi MQTT: " + message);
            } else {
                Log.e(TAG, "MQTT chưa kết nối, không thể gửi tin nhắn.");
            }
        } catch (MqttException e) {
            Log.e(TAG, "Lỗi khi gửi tin nhắn MQTT: " + e.getMessage());
        }
    }

    public String sendRequestAndWaitForResponse(String requestTopic, String subscribeTopic, String payload, int timeoutMillis) throws Exception {
        connectToServer();

        // Dùng CountDownLatch để chờ phản hồi
        CountDownLatch latch = new CountDownLatch(1);
        final StringBuilder responsePayload = new StringBuilder();

        // Đăng ký callback riêng cho topic cần nhận phản hồi
        IMqttMessageListener messageListener = (topic, message) -> {
            if (topic.equals(subscribeTopic)) {
                responsePayload.append(new String(message.getPayload()));
                latch.countDown(); // Báo hiệu đã nhận phản hồi
            }
        };

        client.subscribe(subscribeTopic, 1, messageListener);
        Log.i(TAG, "Subscribed to topic: " + subscribeTopic);

        // Gửi yêu cầu đến topic
        client.publish(requestTopic, new MqttMessage(payload.getBytes()));
        Log.i(TAG, "Published to topic: " + requestTopic);

        // Chờ phản hồi hoặc timeout
        if (!latch.await(timeoutMillis, TimeUnit.MILLISECONDS)) {
            throw new TimeoutException("Timeout waiting for response from: " + subscribeTopic);
        }

        // Hủy đăng ký listener sau khi nhận phản hồi
        client.unsubscribe(subscribeTopic);
        return responsePayload.toString();
    }

    public String getInfoUser(String username, String password, String deviceId) throws Exception {
        String payload = String.format(
                "{ \"username\": \"%s\", \"password\": \"%s\", \"deviceId\": \"%s\" }",
                username, password, deviceId
        );

        String requestTopic = "login/client";
        String responseTopic = "login/client/" + deviceId;

        return sendRequestAndWaitForResponse(requestTopic, responseTopic, payload, TIME_OUT);
    }

    public String editUserInfo(UpdateUserInfo updateUserInfo, String deviceId) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String payload = objectMapper.writeValueAsString(updateUserInfo);

        String requestTopic = "edit-user-info/client";
        String responseTopic = "edit-user-info/client/" + deviceId;

        return sendRequestAndWaitForResponse(requestTopic, responseTopic, payload, TIME_OUT);
    }

    public String getHistory(Alert alert, String deviceId) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String payload = objectMapper.writeValueAsString(alert);

        String requestTopic = "history/client";
        String responseTopic = "history/client/" + deviceId;

        return sendRequestAndWaitForResponse(requestTopic, responseTopic, payload, TIME_OUT);
    }

    public String getPowerConsumption(PowerConsumption powerConsumption) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String payload = objectMapper.writeValueAsString(powerConsumption);

        String requestTopic = "power-consumption/client";
        String responseTopic = "power-consumption/client/" + powerConsumption.getRealDeviceId();

        return sendRequestAndWaitForResponse(requestTopic, responseTopic, payload, TIME_OUT);
    }

    public String createUser(CreateUserRequest createUserRequest) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String payload = objectMapper.writeValueAsString(createUserRequest);

        String requestTopic = "create/client";
        String responseTopic = "create/client/" + createUserRequest.getRealDeviceId();

        return sendRequestAndWaitForResponse(requestTopic, responseTopic, payload, TIME_OUT);
    }

    public String editPassword(EditPasswordRequest editPasswordRequest) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String payload = objectMapper.writeValueAsString(editPasswordRequest);

        String requestTopic = "edit-password/client";
        String responseTopic = "edit-password/client/" + editPasswordRequest.getRealDeviceId();

        return sendRequestAndWaitForResponse(requestTopic, responseTopic, payload, TIME_OUT);
    }

}
