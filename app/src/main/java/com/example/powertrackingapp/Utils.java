package com.example.powertrackingapp;

import com.example.powertrackingapp.model.User;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Utils {

    public static void convertUserInfo(User user, String message) {
        // Parse chuỗi JSON thành JsonObject
        JsonObject jsonObject = JsonParser.parseString(message).getAsJsonObject();

        // Lấy các trường trong JSON
        String fullName = jsonObject.get("fullName").getAsString();
        String address = jsonObject.get("address").getAsString();
        String name = jsonObject.get("username").getAsString();
        int userId = jsonObject.get("userId").getAsInt();
        String role = jsonObject.get("role").getAsString();
        String email = jsonObject.get("email").getAsString();
        String image = jsonObject.get("imageUrl").getAsString();
        String token = jsonObject.get("token").getAsString();

        user.setUsername(name);
        user.setRole(role);
        user.setEmail(email);
        user.setImageUrl(image);
        user.setFullName(fullName);
        user.setAddress(address);
        user.setToken(token);
        user.setUserId(userId);
    }
}
