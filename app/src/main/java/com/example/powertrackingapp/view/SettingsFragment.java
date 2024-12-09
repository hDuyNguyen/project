package com.example.powertrackingapp.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.powertrackingapp.R;
import com.example.powertrackingapp.databinding.SettingBinding;

public class SettingsFragment extends Fragment {
    SettingBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = SettingBinding.inflate(inflater, container, false);

        Button buttonPowerDevice = binding.powerDevice;
        buttonPowerDevice.setBackgroundResource(R.drawable.button_home_selector);
        buttonPowerDevice.setBackgroundTintList(null);

        Button buttonResetPassword = binding.resetPassword;
        buttonResetPassword.setBackgroundResource(R.drawable.button_home_selector);
        buttonResetPassword.setBackgroundTintList(null);

        Button buttonRemoveUser = binding.removeUser;
        buttonRemoveUser.setBackgroundResource(R.drawable.button_home_selector);
        buttonRemoveUser.setBackgroundTintList(null);
        return binding.getRoot();
    }


}
