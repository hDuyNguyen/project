package com.example.powertrackingapp.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.powertrackingapp.R;
import com.example.powertrackingapp.SharedPreferencesHelper;
import com.example.powertrackingapp.controller.Repository;
import com.example.powertrackingapp.controller.Usecase;
import com.example.powertrackingapp.databinding.SettingBinding;
import com.example.powertrackingapp.model.User;
import com.example.powertrackingapp.view.Dialog.ChangeUserInfoDialog;

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

        binding.idAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMenu();
            }
        });

        if (SharedPreferencesHelper.isLoggedIn(requireContext())) {
            User user = SharedPreferencesHelper.getUser(requireContext());
            if (user != null) {
                binding.textName.setText(user.getFullName());
                binding.textEmail.setText(user.getEmail());
                binding.textAddress.setText(user.getAddress());
                binding.textPhone.setText(user.getPhoneNumber());

                Glide.with(this)
                        .load(user.getImageUrl())
                        .placeholder(R.drawable.avatar)
                        .error(R.drawable.avatar)
                        .into(binding.idAvatar);
            }
        }
    }

    private void showMenu() {
        binding.idAvatar.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(getContext(), binding.idAvatar);
            popupMenu.getMenuInflater().inflate(R.menu.user_select, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    if (menuItem.getItemId() == R.id.change_info) {
                        showPopupChangeInfo();
                        return true;
                    }
                    else if (menuItem.getItemId() == R.id.logout) {
                        usecase. logout(requireContext());
                        return true;
                    }
                    else {
                        return false;
                    }
                }
            });

            popupMenu.show();
        });
    }

    private void showPopupChangeInfo() {
        ChangeUserInfoDialog dialog = new ChangeUserInfoDialog();
        dialog.show(getChildFragmentManager(),  "Change userInfo");
    }
}
