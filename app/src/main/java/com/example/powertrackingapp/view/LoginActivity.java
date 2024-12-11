package com.example.powertrackingapp.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.powertrackingapp.R;
import com.example.powertrackingapp.controller.Usecase;
import com.example.powertrackingapp.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.IOException;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private EditText editUserName, editPassword;
    private Button buttonLogin;
    private final Usecase usecase = Usecase.getInstance();
    private String loginMessage;
    private User user = new User();
    public static final String MESSAGE_FAIL = "Thông tin tài khoản hoặc mật khẩu sai";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Kiểm tra trạng thái đăng nhập
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

        if (isLoggedIn) {
            navigateHome();
            return;
        }

        setContentView(R.layout.login);
        editUserName = findViewById(R.id.username);
        editPassword = findViewById(R.id.password);
        buttonLogin = findViewById(R.id.button_login);
        buttonLogin.setBackgroundTintList(null);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = editUserName.getText().toString();
                String password = editPassword.getText().toString();

                try {
                    loginMessage = usecase.login(username, password, MqttClient.generateClientId());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                if (loginMessage.equals(MESSAGE_FAIL)) {
                    TextView textView = findViewById(R.id.toast);
                    textView.setVisibility(View.VISIBLE);
                    textView.setText("Invalid username or password");
                } else {
                    ObjectMapper objectMapper = new ObjectMapper();
                    Map<String, String> map = null;
                    try {
                        map = objectMapper.readValue(loginMessage, Map.class);
                        String role = map.get("role");
                        String name = map.get("username");
                        user.setUsername(name);
                        user.setRole(role);

                        Gson gson = new Gson();
                        String jsonUser = gson.toJson(user);

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("isLoggedIn", true);
                        editor.putString("userInfo", jsonUser);
                        editor.apply();

                        assert role != null;
                        if (role.equals("ROLE_USER")) {
                            navigateHome();
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    private void navigateHome() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
