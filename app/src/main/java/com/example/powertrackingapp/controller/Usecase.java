package com.example.powertrackingapp.controller;

import static android.content.Context.MODE_PRIVATE;
import static com.example.powertrackingapp.AppConstant.IS_LOGGED_IN;
import static com.example.powertrackingapp.AppConstant.SHARED_REF;
import static com.example.powertrackingapp.AppConstant.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.powertrackingapp.model.Alert;
import com.example.powertrackingapp.model.CreateUserRequest;
import com.example.powertrackingapp.model.DeleteUserRequest;
import com.example.powertrackingapp.model.EditPasswordRequest;
import com.example.powertrackingapp.model.GetAllUSerRequest;
import com.example.powertrackingapp.model.PowerConsumption;
import com.example.powertrackingapp.model.UpdateUserInfo;
import com.example.powertrackingapp.view.LoginActivity;

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

    public String login(String username, String password, String deviceId) throws Exception {
        String userInfo = repository.getInfoUser(username, password, deviceId);
        if (userInfo == null) {
            Log.i(TAG, "userInfo null");
        }
        return userInfo;
    }

    public void logout(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_REF, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(IS_LOGGED_IN, false); // Xóa trạng thái đăng nhập
        editor.apply();

        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    public String getHistory(Alert alert, String deviceId) throws Exception {
        String history = repository.getHistory(alert, deviceId);
        if (history == null) {
            Log.i(TAG, "userInfo null");
        }
        return history;
    }

    public String editUserInfo(UpdateUserInfo updateUserInfo, String deviceId) throws Exception {
        String result =  repository.editUserInfo(updateUserInfo, deviceId);
        if (result == null) {
            Log.i(TAG, "update info false");
        }
        return result;
    }

    public String getPowerConsumption(PowerConsumption powerConsumption) throws Exception {
        String result =  repository.getPowerConsumption(powerConsumption);
        if (result == null) {
            Log.i(TAG, "data consumption is null");
        }
        return result;
    }

    public String createUser(CreateUserRequest createUserRequest) throws Exception {
        String result =  repository.createUser(createUserRequest);
        if (result == null) {
            Log.i(TAG, "create is false");
        }
        return result;
    }

    public String editPassword(EditPasswordRequest editPasswordRequest) throws Exception {
        String result =  repository.editPassword(editPasswordRequest);
        if (result == null) {
            Log.i(TAG, "create is false");
        }
        return result;
    }

    public String deleteUser(DeleteUserRequest deleteUserRequest) throws Exception {
        String result =  repository.deleteUser(deleteUserRequest);
        if (result == null) {
            Log.i(TAG, "create is false");
        }
        return result;
    }

    public String getAllUser(GetAllUSerRequest getAllUSerRequest, String deviceId) throws Exception {
        String result =  repository.getAllUSer(getAllUSerRequest, deviceId);
        if (result == null) {
            Log.i(TAG, "create is false");
        }
        return result;
    }
}
