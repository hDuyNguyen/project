package com.example.powertrackingapp.service;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.content.Context;
import android.util.Log;

import com.example.powertrackingapp.SharedPreferencesHelper;
import com.example.powertrackingapp.controller.Repository;
import com.example.powertrackingapp.model.SendToken;
import com.example.powertrackingapp.model.User;
import com.example.powertrackingapp.view.LoginActivity;
import com.google.firebase.messaging.FirebaseMessaging;

public class FirebaseToken {
    private static final String TAG = "FirebaseToken";
    private static final String MQTT_TOPIC = "device/fcm_token"; // Server subscribe topic này
    private User user;

    public static void sendTokenToServer(Context context) {
        SendToken sendToken = new SendToken();
        if (SharedPreferencesHelper.isLoggedIn(context)) {
            User user1 = SharedPreferencesHelper.getUser(context);
            FirebaseMessaging.getInstance().getToken()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult() != null) {
                            String token = task.getResult();
                            Log.d(TAG, "FCM Token: " + token);
                            sendToken.setFirebaseToken(token);
                            assert user1 != null;
                            sendToken.setToken(user1.getToken());
                            Repository.getInstance().sendTokenToServer(MQTT_TOPIC, sendToken); // Gửi token qua MQTT
                            SharedPreferencesHelper.saveTokenNotify(context, token);
                            LoginActivity.isSendToken = true;
                        } else {
                            Log.e(TAG, "Lấy FCM Token thất bại");
                        }
                    });
        }
    }
}
