package com.example.powertrackingapp.controller;

import static android.content.Context.MODE_PRIVATE;

import static com.example.powertrackingapp.AppConstant.IS_LOGGED_IN;
import static com.example.powertrackingapp.AppConstant.USER_SESSION;
import static com.example.powertrackingapp.AppConstant.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.powertrackingapp.model.User;
import com.example.powertrackingapp.view.LoginActivity;
import com.example.powertrackingapp.view.MainActivity;
import com.google.gson.Gson;

import org.eclipse.paho.client.mqttv3.MqttException;

public class Usecase {

    private static volatile Usecase instance;
    private final Repository repository = Repository.getInstance();

    public static Usecase getInstance() {
        if (instance == null) {
            synchronized (Usecase.class) {
                if (instance == null) {
                    instance = new Usecase();
                }
            }
        }
        return instance;
    }

    public void disconnect() {
        repository.disconnect();
    }

    public String login(String username, String password, String deviceId) throws MqttException, InterruptedException {
        String userInfo = repository.getInfoUser(username, password, deviceId);
        if (userInfo == null) {
            Log.i(TAG, "userInfo null");
        }
        return userInfo;
    }

    public void logout(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(USER_SESSION, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(IS_LOGGED_IN, false); // Xóa trạng thái đăng nhập
        editor.apply();

        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }
}
