package com.example.powertrackingapp.view.Dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.example.powertrackingapp.R;
import com.example.powertrackingapp.SharedPreferencesHelper;
import com.example.powertrackingapp.databinding.ChangeUserInfoBinding;
import com.example.powertrackingapp.model.User;

public class ChangeUserInfoDialog extends DialogFragment {
    ChangeUserInfoBinding binding;

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
            User user = SharedPreferencesHelper.getUser(requireContext());
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
            //todo
        });
    }
}
