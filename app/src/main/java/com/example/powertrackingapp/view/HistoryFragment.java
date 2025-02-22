package com.example.powertrackingapp.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.powertrackingapp.SharedPreferencesHelper;
import com.example.powertrackingapp.databinding.HistoryBinding;
import com.example.powertrackingapp.model.User;

public class HistoryFragment extends Fragment {
    HistoryBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = HistoryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getUserInfo();
    }

    @Override
    public void onResume() {
        super.onResume();
        getUserInfo();
    }

    private void getUserInfo() {
        if (SharedPreferencesHelper.isLoggedIn(requireContext())) {
            User user = SharedPreferencesHelper.getUser(requireContext());
            if (user != null) {
                binding.userName.setText(user.getFullName());
                binding.userAddress.setText(user.getAddress());
            }
        }
    }
}
