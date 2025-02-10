package com.example.powertrackingapp.view.Dialog;

import static com.example.powertrackingapp.AppConstant.DEVICE_ID;
import static com.example.powertrackingapp.AppConstant.WRONG_OLD_PASSWORD;
import static com.example.powertrackingapp.AppConstant.WRONG_REPEAT_PASSWORD;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.powertrackingapp.R;
import com.example.powertrackingapp.SharedPreferencesHelper;
import com.example.powertrackingapp.Utils;
import com.example.powertrackingapp.controller.Usecase;
import com.example.powertrackingapp.databinding.ChangeUserInfoBinding;
import com.example.powertrackingapp.databinding.DialogEditPasswordBinding;
import com.example.powertrackingapp.model.EditPasswordRequest;
import com.example.powertrackingapp.model.UpdateUserInfo;
import com.example.powertrackingapp.model.User;

public class EditPasswordDialog extends DialogFragment {
    DialogEditPasswordBinding binding;
    Usecase usecase = Usecase.getInstance();
    User user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogEditPasswordBinding.inflate(inflater, container, false);
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
        }

        binding.editPassDialogCancelButton.setOnClickListener(v -> {
            dismiss();
        });

        binding.editPassDialogOkButton.setOnClickListener(v -> {

            EditPasswordRequest editPasswordRequest = new EditPasswordRequest();
            editPasswordRequest.setToken(user.getToken());
            editPasswordRequest.setRealDeviceId(DEVICE_ID);
            editPasswordRequest.setNewPassword(String.valueOf(binding.newPassword.getText()));

            if (compareOldPassword(String.valueOf(binding.oldPassword.getText()))) {
                try {
                    String result = usecase.editPassword(editPasswordRequest);
                    dismiss();
                    if (result != null) {
                        Toast.makeText(getContext(), "Edit successful! ", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getContext(), "Edit failure!", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                binding.notificationEditPassword.setVisibility(View.VISIBLE);
                binding.notificationEditPassword.setText(WRONG_OLD_PASSWORD);
            }
        });

        compareNewPassword(binding.newPassword.getText().toString(), binding.repeatNewPassword.getText().toString());
    }

    private boolean compareOldPassword(String oldPassword) {
        String password = "";
        if (SharedPreferencesHelper.isLoggedIn(requireContext())) {
            password = SharedPreferencesHelper.getPassword(requireContext());
        }
        return oldPassword.equals(password);
    }

    private void compareNewPassword(String newPassword, String repeat) {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Lấy giá trị mới nhất của 2 EditText
                String newPassword = binding.newPassword.getText().toString().trim();
                String repeat = binding.repeatNewPassword.getText().toString().trim();

                if (!newPassword.isEmpty() && !repeat.isEmpty()) {
                   if (repeat.equals(newPassword)) {
                       binding.repeatNewPassword.setBackgroundResource(R.drawable.edit_text_border);
                       binding.notificationEditPassword.setVisibility(View.GONE);
                   } else {
                       binding.repeatNewPassword.setBackgroundResource(R.drawable.wrong_edit_text_border);
                       binding.notificationEditPassword.setVisibility(View.VISIBLE);
                       binding.notificationEditPassword.setText(WRONG_REPEAT_PASSWORD);
                   }
                }
                else {
                    binding.repeatNewPassword.setBackgroundResource(R.drawable.edit_text_border);
                    binding.notificationEditPassword.setVisibility(View.GONE);
                }
            }
        };
        // Gắn TextWatcher vào cả 2 EditText
        binding.newPassword.addTextChangedListener(textWatcher);
        binding.repeatNewPassword.addTextChangedListener(textWatcher);
    }
}
