package com.example.powertrackingapp.view.Dialog;

import static com.example.powertrackingapp.AppConstant.DEVICE_ID;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.powertrackingapp.AppConstant;
import com.example.powertrackingapp.R;
import com.example.powertrackingapp.SharedPreferencesHelper;
import com.example.powertrackingapp.Utils;
import com.example.powertrackingapp.controller.Usecase;
import com.example.powertrackingapp.databinding.ChangeUserInfoBinding;
import com.example.powertrackingapp.model.UpdateUserInfo;
import com.example.powertrackingapp.model.User;
import com.example.powertrackingapp.view.SettingsFragment;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ChangeUserInfoDialog extends DialogFragment {
    ChangeUserInfoBinding binding;
    Usecase usecase = Usecase.getInstance();
    User user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ChangeUserInfoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Window window = getDialog().getWindow();
        if (window != null) {
            // Tùy chỉnh kích thước theo nhu cầu
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = (int) (getResources().getDisplayMetrics().widthPixels * 0.9); // Chiều rộng 90% màn hình
            params.height = (int) (getResources().getDisplayMetrics().heightPixels * 0.5); // Chiều cao tự động
            window.setAttributes(params);
            window.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));
        }

        if (SharedPreferencesHelper.isLoggedIn(requireContext())) {
            user = SharedPreferencesHelper.getUser(requireContext());
            if (user != null) {
                binding.editUsername.setText(user.getFullName());
                binding.editEmail.setText(user.getEmail());
                binding.editAddress.setText(user.getAddress());
                binding.editPhoneNumber.setText(user.getPhoneNumber());
            }
        }

        binding.cancel.setOnClickListener(v -> {
            dismiss();
        });

        binding.ok.setOnClickListener(v -> {
            dismiss();
            UpdateUserInfo updateUserInfo = new UpdateUserInfo();
            updateUserInfo.setUsersId(user.getUserId());
            updateUserInfo.setToken(user.getToken());

            updateUserInfo.setFullName(String.valueOf(binding.editUsername.getText()));
            updateUserInfo.setPhone(String.valueOf(binding.editPhoneNumber.getText()));
            updateUserInfo.setEmail(String.valueOf(binding.editEmail.getText()));
            updateUserInfo.setAddress(String.valueOf(binding.editAddress.getText()));
            updateUserInfo.setImageUrl("abc");
            updateUserInfo.setRealDeviceId(DEVICE_ID);


            try {
                String result = usecase.editUserInfo(updateUserInfo, DEVICE_ID);
                if (result != null) {
                    Toast.makeText(getContext(), "Edit successful!", Toast.LENGTH_LONG).show();
                    refreshData();
                    Fragment fragment = getParentFragmentManager().findFragmentByTag("SettingsFragment");
                    if (fragment instanceof SettingsFragment) {
                        ((SettingsFragment) fragment).getUserInfo();
                    }
                } else {
                    Toast.makeText(getContext(), "Edit failure!", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void refreshData() throws Exception {
        if (SharedPreferencesHelper.isLoggedIn(requireContext())) {
            String username = SharedPreferencesHelper.getUsername(requireContext());
            String password = SharedPreferencesHelper.getPassword(requireContext());

            String message = usecase.login(username, password, DEVICE_ID);
            Utils.convertUserInfo(user, message);
            SharedPreferencesHelper.saveUser(requireContext(), true, user, username, password);
        }
    }
}
