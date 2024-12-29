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
import com.example.powertrackingapp.SharedPreferencesHelper;
import com.example.powertrackingapp.controller.Repository;
import com.example.powertrackingapp.controller.Usecase;
import com.example.powertrackingapp.databinding.SettingBinding;
import com.example.powertrackingapp.model.User;

public class SettingsFragment extends Fragment {
    SettingBinding binding;
    private final Usecase usecase = Usecase.getInstance();

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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.idAvatar.setOnClickListener(v -> {
            usecase.logout(requireContext());
        });

        if (SharedPreferencesHelper.isLoggedIn(requireContext())) {
            User user = SharedPreferencesHelper.getUser(requireContext());
            if (user != null) {
                binding.textName.setText(user.getFullName());
                binding.textAddress.setText(user.getAddress());
            }
        }

    }
}
