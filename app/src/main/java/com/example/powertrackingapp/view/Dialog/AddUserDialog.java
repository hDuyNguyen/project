package com.example.powertrackingapp.view.Dialog;

import static com.example.powertrackingapp.AppConstant.DEVICE_ID;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.powertrackingapp.SharedPreferencesHelper;
import com.example.powertrackingapp.controller.Usecase;
import com.example.powertrackingapp.databinding.AddUserBinding;
import com.example.powertrackingapp.model.CreateUserRequest;
import com.example.powertrackingapp.model.UpdateUserInfo;
import com.example.powertrackingapp.model.User;

public class AddUserDialog extends DialogFragment {
    AddUserBinding binding;
    Usecase usecase = Usecase.getInstance();
    User user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = AddUserBinding.inflate(inflater, container, false);
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
            params.height = (int) (getResources().getDisplayMetrics().heightPixels * 0.7); // Chiều cao tự động
            window.setAttributes(params);
            window.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));
        }

        if (SharedPreferencesHelper.isLoggedIn(requireContext())) {
            user = SharedPreferencesHelper.getUser(requireContext());
        }

        binding.cancelUserAddDialog.setOnClickListener(v -> {
            dismiss();
        });

        binding.okUserAddDialog.setOnClickListener(v -> {
            dismiss();
            CreateUserRequest createUserRequest = new CreateUserRequest();
            createUserRequest.setToken(user.getToken());
            createUserRequest.setUsername(String.valueOf(binding.addAccountName.getText()));
            createUserRequest.setPassword(String.valueOf(binding.addPassword.getText()));
            createUserRequest.setPhone(String.valueOf(binding.addPhoneNumber.getText()));
            createUserRequest.setRealDeviceId(DEVICE_ID);

            try {
                String result = usecase.createUser(createUserRequest);
                if (result != null) {
                    Toast.makeText(getContext(), "Edit successful!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), "Edit failure!", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
