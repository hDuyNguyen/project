package com.example.powertrackingapp.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.powertrackingapp.R;
import com.example.powertrackingapp.databinding.AdminBinding;
import com.example.powertrackingapp.databinding.HomeBinding;

public class AdminSetting extends Fragment {
    AdminBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = AdminBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @SuppressLint("ResourceType")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button manageUser = view.findViewById(R.id.manage_user);
        manageUser.setBackgroundResource(R.drawable.button_home_selector);
        manageUser.setBackgroundTintList(null);

        Button manageTechnical = view.findViewById(R.id.manage_technical);
        manageTechnical.setBackgroundResource(R.drawable.button_home_selector);
        manageTechnical.setBackgroundTintList(null);

        binding.manageUser.setOnClickListener(v -> {
            Fragment userListFragment =  new UserListFragment();
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container_view, userListFragment)
                    .addToBackStack(null)
                    .commit();
        });

        binding.manageTechnical.setOnClickListener(v -> {
            Fragment technicalListFragment =  new TechnicalListFragment();
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container_view, technicalListFragment)
                    .addToBackStack(null)
                    .commit();
        });
    }
}
