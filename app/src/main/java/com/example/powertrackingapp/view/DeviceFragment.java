package com.example.powertrackingapp.view;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.powertrackingapp.R;
import com.example.powertrackingapp.databinding.DetailDeviceBinding;
import com.example.powertrackingapp.databinding.DeviceBinding;

public class DeviceFragment extends Fragment {
    DeviceBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DeviceBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        binding.device1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.i("duy", "onClick: ");
//                getParentFragmentManager().beginTransaction()
//                        .replace(R.id.all_device, new DetailDeviceFragment())
//                        .addToBackStack(null)
//                        .commit();
//            }
//        });
    }
}
