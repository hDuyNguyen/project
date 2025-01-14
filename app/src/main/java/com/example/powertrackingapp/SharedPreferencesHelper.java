package com.example.powertrackingapp;

import static com.example.powertrackingapp.AppConstant.IS_LOGGED_IN;
import static com.example.powertrackingapp.AppConstant.PASSWORD;
import static com.example.powertrackingapp.AppConstant.SHARED_REF;
import static com.example.powertrackingapp.AppConstant.USERNAME;
import static com.example.powertrackingapp.AppConstant.USER_INFO;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.powertrackingapp.model.User;
import com.google.gson.Gson;

public class SharedPreferencesHelper {

    public static void saveUser(Context context, boolean isLoggedIn, User user, String username, String password) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_REF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(IS_LOGGED_IN, isLoggedIn);
        editor.putString(USERNAME, username);
        editor.putString(PASSWORD, password);

        Gson gson = new Gson();
        String jsonUser = gson.toJson(user);
        editor.putString(USER_INFO, jsonUser);

        editor.apply();
    }

    public static User getUser(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_REF, Context.MODE_PRIVATE);

        String jsonUser = sharedPreferences.getString(USER_INFO, null);
        if (jsonUser != null) {
            Gson gson = new Gson();
            return gson.fromJson(jsonUser, User.class);
        }
        return null;
    }

    public static boolean isLoggedIn(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_REF, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(IS_LOGGED_IN, false);
    }

    public static String getUsername(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_REF, Context.MODE_PRIVATE);
        return sharedPreferences.getString(USERNAME, null);
    }

    public static String getPassword(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_REF, Context.MODE_PRIVATE);
        return sharedPreferences.getString(PASSWORD, null);
    }
}
