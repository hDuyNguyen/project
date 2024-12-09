package com.example.powertrackingapp.controller;

public class Usecase {

    public void login(String username, String password, String deviceId) {
        String requestTopic = "login/client";
        String messageRequest = String.format(
                "{ \"username\": \"%s\", \"password\": \"%s\", \"deviceId\": \"%s\" }",
                username, password, deviceId
        );
    }
}
