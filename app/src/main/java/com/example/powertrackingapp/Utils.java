package com.example.powertrackingapp;

import com.example.powertrackingapp.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

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

    public static List<String> convertJsonToStringHistory(String json) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(json);

        // Lấy danh sách 'data'
        JsonNode dataArray = rootNode.get("data");

        List<String> results = new ArrayList<>();
        if (dataArray.isArray()) {
            for (JsonNode item : dataArray) {
                // Lấy createAt
                JsonNode createAtArray = item.get("createAt");
                int year = createAtArray.get(0).asInt();
                int month = createAtArray.get(1).asInt();
                int day = createAtArray.get(2).asInt();
                int hour = createAtArray.get(3).asInt();
                int minute = createAtArray.get(4).asInt();
                int second = createAtArray.get(5).asInt();

                // Lấy message
                String message = item.get("message").asText();

                // Tạo chuỗi kết quả
                String formattedDate = String.format("%d/%d/%d - %d:%d:%d: %s", day, month, year, hour, minute, second, message);
                results.add(formattedDate);
            }
        }
        return results;
    }
}
