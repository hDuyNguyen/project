package com.example.powertrackingapp.view;

import static com.example.powertrackingapp.AppConstant.DEVICE_ID;
import static com.example.powertrackingapp.AppConstant.IS_LOGGED_IN;
import static com.example.powertrackingapp.AppConstant.MESSAGE_FAIL;
import static com.example.powertrackingapp.AppConstant.MESSAGE_TOAST;
import static com.example.powertrackingapp.AppConstant.ROLE_USER;
import static com.example.powertrackingapp.AppConstant.SHARED_REF;

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
import com.example.powertrackingapp.SharedPreferencesHelper;
import com.example.powertrackingapp.Utils;
import com.example.powertrackingapp.controller.Usecase;
import com.example.powertrackingapp.model.User;
import com.example.powertrackingapp.service.FirebaseToken;

public class LoginActivity extends AppCompatActivity {
    private EditText editUserName, editPassword;
    private Button buttonLogin;
    private final Usecase usecase = Usecase.getInstance();
    private User user = new User();
    public static boolean isSendToken = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Kiểm tra trạng thái đăng nhập
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_REF, MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean(IS_LOGGED_IN, false);

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
                String loginMessage;
                String username = editUserName.getText().toString();
                String password = editPassword.getText().toString();

                try {
                    loginMessage = usecase.login(username, password, DEVICE_ID);
                    isSendToken = false;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                if (loginMessage.equals(MESSAGE_FAIL)) {
                    TextView textView = findViewById(R.id.toast);
                    textView.setVisibility(View.VISIBLE);
                    textView.setText(MESSAGE_TOAST);
                } else {
                    Utils.convertUserInfo(user, loginMessage);

                    SharedPreferencesHelper.saveUser(getApplicationContext(), true, user, username, password);

                    if (user.getRole().equals(ROLE_USER)) {
                        navigateHome();
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
