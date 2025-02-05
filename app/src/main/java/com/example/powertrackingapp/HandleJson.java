package com.example.powertrackingapp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

public class HandleJson {
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
