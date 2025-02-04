package com.example.powertrackingapp.service;

import android.content.Context;
import android.util.Log;

import com.example.powertrackingapp.SharedPreferencesHelper;
import com.example.powertrackingapp.controller.Repository;
import com.google.firebase.messaging.FirebaseMessaging;

public class FirebaseToken {
    private static final String TAG = "FirebaseToken";
    private static final String MQTT_TOPIC = "device/fcm_token"; // Server subscribe topic này

    public static void sendTokenToServer(Context context) {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        String token = task.getResult();
                        Log.d(TAG, "FCM Token: " + token);
                        Repository.getInstance().sendTokenToServer(MQTT_TOPIC, token); // Gửi token qua MQTT
                        SharedPreferencesHelper.saveTokenNotify(context, token);
                    } else {
                        Log.e(TAG, "Lấy FCM Token thất bại");
                    }
                });
    }
}
