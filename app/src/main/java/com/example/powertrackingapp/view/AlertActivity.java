package com.example.powertrackingapp.view;

import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.powertrackingapp.R;

public class AlertActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Gắn layout cho AlertActivity
        setContentView(R.layout.alert);

        // Xử lý nút "Đóng" (nếu có)
        Button closeButton = findViewById(R.id.closeButton);
        closeButton.setOnClickListener(view -> finish());
    }
}
