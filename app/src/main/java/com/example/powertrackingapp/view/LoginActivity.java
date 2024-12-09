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

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

public class LoginActivity extends AppCompatActivity {
    private EditText editUserName, editPassword;
    private Button buttonLogin;
    private final Usecase usecase = Usecase.getInstance();

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

                User user = new User();
                try {
                    user = usecase.login(username, password, MqttClient.generateClientId());
                } catch (MqttException e) {
                    throw new RuntimeException(e);
                }
                if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("isLoggedIn", true);
                    editor.apply();

                    navigateHome();
                } else {
                    TextView textView = findViewById(R.id.toast);
                    textView.setVisibility(View.VISIBLE);
                    textView.setText("Invalid username or password");
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
