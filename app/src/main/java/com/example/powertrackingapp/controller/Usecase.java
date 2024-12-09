package com.example.powertrackingapp.controller;

import com.example.powertrackingapp.model.User;
import com.google.gson.Gson;

import org.eclipse.paho.client.mqttv3.MqttException;

public class Usecase {

    private static volatile Usecase instance;
    private final Repository repository = Repository.getInstance();

    public static Usecase getInstance() {
        if (instance == null) {
            synchronized (Repository.class) {
                if (instance == null) {
                    instance = new Usecase();
                }
            }
        }
        return instance;
    }

    public void connect() {
        repository.connectToServer();
    }

    public void disconnect() {
        repository.disconnect();
    }

    public User login(String username, String password, String deviceId) throws MqttException {
        String userInfo = repository.getInfoUser(username, password, deviceId);

        Gson gson = new Gson();
        return gson.fromJson(userInfo, User.class);
    }
}
