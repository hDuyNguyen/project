package com.example.powertrackingapp.view;

import static com.example.powertrackingapp.AppConstant.ROLE_ADMIN;
import static com.example.powertrackingapp.AppConstant.ROLE_TECHNICAL;
import static com.example.powertrackingapp.AppConstant.ROLE_USER;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.powertrackingapp.R;
import com.example.powertrackingapp.SharedPreferencesHelper;
import com.example.powertrackingapp.controller.Usecase;
import com.example.powertrackingapp.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private final Usecase usecase = Usecase.getInstance();
    private User user;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        if (SharedPreferencesHelper.isLoggedIn(this)) {
            user = SharedPreferencesHelper.getUser(this);
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
                openFragment(new AdminSetting());
                switch (user.getRole()) {
                    case ROLE_USER:
                        openFragment(new SettingsFragment());
                        break;
                    case ROLE_ADMIN:
                    case ROLE_TECHNICAL:
                        openFragment(new AdminSetting());
                        break;
                }
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
