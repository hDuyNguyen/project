package com.example.powertrackingapp.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.powertrackingapp.SharedPreferencesHelper;
import com.example.powertrackingapp.controller.DatePickerController;
import com.example.powertrackingapp.databinding.DeviceAbnormalHistoryBinding;
import com.example.powertrackingapp.model.DatePickerModel;
import com.example.powertrackingapp.model.User;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DeviceAbnormalHistory extends Fragment implements DatePicker.DatePickerListener {
    DeviceAbnormalHistoryBinding binding;
    DatePickerModel datePickerModel;
    DatePickerController datePickerController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DeviceAbnormalHistoryBinding.inflate(inflater, container, false);

        datePickerModel = new DatePickerModel();
        datePickerController = DatePickerController.getInstance(datePickerModel);

        binding.radioGroup.check(binding.radioA.getId());
        // Lấy ngày giờ hiện tại
        Calendar calendar = Calendar.getInstance();

        // Định dạng ngày giờ
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDateTime = formatter.format(calendar.getTime());
        binding.startDate.setText(formattedDateTime);
        binding.endDate.setText(formattedDateTime);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setBackGroundButton();
        binding.back.setOnClickListener(view1 -> getParentFragmentManager().popBackStack());

        binding.calendarStart.setOnClickListener(view2 -> {
            DatePicker datePicker = DatePicker.newInstance(binding.calendarStart.getId());
            datePicker.show(getChildFragmentManager(), "DATE START PICK");
        });

        binding.calendarEnd.setOnClickListener(view3 -> {
            DatePicker datePicker = DatePicker.newInstance(binding.calendarEnd.getId());
            datePicker.show(getChildFragmentManager(), "DATE END PICK");
        });
    }

    private void setBackGroundButton() {
        Button button1 = binding.tb1;
        button1.setBackgroundTintList(null);

        Button button2 = binding.tb2;
        button2.setBackgroundTintList(null);

        Button button3 = binding.tb3;
        button3.setBackgroundTintList(null);

        Button button4 = binding.tb4;
        button4.setBackgroundTintList(null);

        Button button5 = binding.tb5;
        button5.setBackgroundTintList(null);

        Button button6 = binding.tb6;
        button6.setBackgroundTintList(null);
    }


    @Override
    public void onDateSet(int viewId, int year, int month, int dayOfMonth) {
        String formatDate = DatePicker.formatDate(year, month, dayOfMonth);

        if (viewId == binding.calendarStart.getId()) {
            binding.startDate.setText(formatDate);
            datePickerController.setStartDate(formatDate);
        }
        if (viewId == binding.calendarEnd.getId()) {
            binding.endDate.setText(formatDate);
            datePickerController.setEndDate(formatDate);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        String startDate = datePickerController.getStartDate();
        String endDate = datePickerController.getEndDate();

        if (startDate != null) {
            binding.startDate.setText(startDate);
        }

        if (endDate != null) {
            binding.endDate.setText(endDate);
        }
    }
}
