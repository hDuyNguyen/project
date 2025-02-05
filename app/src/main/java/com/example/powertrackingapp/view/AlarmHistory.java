package com.example.powertrackingapp.view;

import static com.example.powertrackingapp.AppConstant.TAG;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.powertrackingapp.AppConstant;
import com.example.powertrackingapp.HandleJson;
import com.example.powertrackingapp.R;
import com.example.powertrackingapp.SharedPreferencesHelper;
import com.example.powertrackingapp.controller.DatePickerController;
import com.example.powertrackingapp.controller.Usecase;
import com.example.powertrackingapp.databinding.AlarmHistoryBinding;
import com.example.powertrackingapp.model.Alert;
import com.example.powertrackingapp.model.DatePickerModel;
import com.example.powertrackingapp.model.HistoryAdapter;
import com.example.powertrackingapp.model.User;
import com.example.powertrackingapp.view.Dialog.DatePicker;

import org.eclipse.paho.client.mqttv3.MqttClient;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AlarmHistory extends Fragment implements DatePicker.DatePickerListener {
    AlarmHistoryBinding binding;
    DatePickerModel datePickerModel;
    DatePickerController datePickerController;
    User user;
    List<String> historyList = new ArrayList<>();

    private final Usecase usecase = Usecase.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = AlarmHistoryBinding.inflate(inflater, container, false);
        user = SharedPreferencesHelper.getUser(requireContext());
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
        binding.deviceAbnormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container_history, new DeviceAbnormalHistory())
                        .addToBackStack(null)
                        .commit();
            }
        });

        binding.calendarStart.setOnClickListener(view1 -> {
            DatePicker datePicker = DatePicker.newInstance(binding.calendarStart.getId());
            datePicker.show(getChildFragmentManager(), "DATE START PICK");
        });

        binding.calendarEnd.setOnClickListener(view12 -> {
            DatePicker datePicker = DatePicker.newInstance(binding.calendarEnd.getId());
            datePicker.show(getChildFragmentManager(), "DATE END PICK");
        });
    }

    private void setBackGroundButton() {
        Button button1 = binding.systemAbnormal;
        button1.setBackgroundTintList(null);

        Button button2 = binding.overloadSystem;
        button2.setBackgroundTintList(null);

        Button button3 = binding.deviceAbnormal;
        button3.setBackgroundTintList(null);
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

        try {
            showHistory(
                    DatePicker.convertStringToLocalDate(binding.startDate.getText().toString()),
                    DatePicker.convertStringToLocalDate(binding.endDate.getText().toString()));

            binding.historyList.setAdapter(new HistoryAdapter(getActivity(), this, historyList));
        } catch (Exception e) {
            Log.e(TAG, "onDateSet: ", e);
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

    private void showHistory(LocalDate startDate, LocalDate endDate) throws Exception {
        Alert alert = new Alert();
        alert.setUserId(user.getUserId());
        alert.setToken(user.getToken());
        alert.setRealDeviceId(AppConstant.DEVICE_ID);
        alert.setStartDate(startDate);
        alert.setEndDate(endDate);
        alert.setPageNumber(0);
        alert.setPageSize(10);
        String history = usecase.getHistory(alert, AppConstant.DEVICE_ID);
        Log.i(TAG, "showHistory: " + history);

        if (history != null) {
            Thread.sleep(1000);
            historyList = HandleJson.convertJsonToStringHistory(history);
        }
    }
}
