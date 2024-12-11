package com.example.powertrackingapp.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.powertrackingapp.R;
import com.example.powertrackingapp.controller.Repository;
import com.example.powertrackingapp.controller.Usecase;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.eclipse.paho.client.mqttv3.MqttException;

public class MainActivity extends AppCompatActivity {

    private final Usecase usecase = Usecase.getInstance();

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        setClickInBottomNavigation();
    }

    private void setClickInBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.menu_home) {
                openFragment(new HomeFragment());
                return true;
            }
            if (item.getItemId() == R.id.menu_device) {
                openFragment(new DeviceFragment());
                return true;
            }
            if (item.getItemId() == R.id.menu_history) {
                openFragment(new HistoryFragment());
                return true;
            }
            if (item.getItemId() == R.id.menu_settings) {
                openFragment(new SettingsFragment());
                return true;
            }
            return false;
        });
    }

    private void openFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container_view, fragment)
                .commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        usecase.disconnect();
    }
}
