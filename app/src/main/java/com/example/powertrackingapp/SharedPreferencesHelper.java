package com.example.powertrackingapp;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

public class SharedPreferencesHelper {
    private static final String PREF_NAME = "userInfo";

    public static void saveObject(Context context, String key, Object object) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String jsonString = gson.toJson(object);
        editor.putString(key, jsonString);
        editor.apply();
    }


    public static <T> T getObject(Context context, String key, Class<T> classType) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String jsonString = sharedPreferences.getString(key, null);

        if (jsonString == null) {
            return null;
        }

        Gson gson = new Gson();
        return gson.fromJson(jsonString, classType);
    }


}
