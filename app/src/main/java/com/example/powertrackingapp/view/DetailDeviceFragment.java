package com.example.powertrackingapp.view;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.powertrackingapp.controller.DatePickerController;
import com.example.powertrackingapp.databinding.DetailDeviceBinding;
import com.example.powertrackingapp.model.DatePickerModel;
import com.example.powertrackingapp.view.Dialog.DatePicker;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class DetailDeviceFragment extends Fragment implements DatePicker.DatePickerListener {
    DetailDeviceBinding binding;
    DatePickerModel datePickerModel;
    DatePickerController datePickerController;

//    private final List<String> value = Arrays.asList("27/11", "28/11", "29/11");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DetailDeviceBinding.inflate(inflater, container, false);

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

        binding.calendarStart.setOnClickListener(view1 -> {
            DatePicker datePicker = DatePicker.newInstance(binding.calendarStart.getId());
            datePicker.show(getChildFragmentManager(), "DATE START PICK");
        });

        binding.calendarEnd.setOnClickListener(view2 -> {
            DatePicker datePicker = DatePicker.newInstance(binding.calendarEnd.getId());
            datePicker.show(getChildFragmentManager(), "DATE END PICK");
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        createChart();

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().popBackStack();
            }
        });
    }

    private void createChart() {
        BarChart barChart = binding.chart;
        barChart.getAxisRight().setDrawLabels(false);

        List<Integer> colors = new ArrayList<>();
        ArrayList<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Random random = new Random();
            int randomNumber = random.nextInt(991) + 10;
            entries.add(new BarEntry(i, randomNumber));
            colors.add(getRandomColor());
        }

        YAxis yAxis = barChart.getAxisLeft();
        yAxis.setAxisMaximum(0f);
        yAxis.setAxisMaximum(1000f);
        yAxis.setAxisLineWidth(2f);
        yAxis.setAxisLineColor(Color.BLACK);
        yAxis.setLabelCount(20);

        BarDataSet barDataSet = new BarDataSet(entries, "Type");
        barDataSet.setColors(colors);

        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);

        barChart.getDescription().setEnabled(false);
        barChart.invalidate();

//        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(value));
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getXAxis().setAxisLineColor(Color.BLACK);
        barChart.getXAxis().setGranularity(1f);
        barChart.getAxisLeft().setDrawLabels(false);
        barChart.getAxisLeft().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return value + "Kwh";
            }
        });
        barChart.getXAxis().setGranularityEnabled(true);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.setScaleXEnabled(false);
    }

    private int getRandomColor() {
        // Giá trị RGB của màu #86a9fc (màu sáng hơn)
        int startR = 0, startG = 41, startB = 135;

        // Giá trị RGB của màu #074ff7 (màu tối hơn)
        int endR = 12, endG = 79, endB = 255;

        // Đảm bảo khoảng giá trị hợp lệ
        int minR = Math.min(startR, endR);
        int maxR = Math.max(startR, endR);
        int minG = Math.min(startG, endG);
        int maxG = Math.max(startG, endG);
        int minB = Math.min(startB, endB);
        int maxB = Math.max(startB, endB);

        // Sinh giá trị ngẫu nhiên cho mỗi kênh màu
        Random random = new Random();
        int r = minR + random.nextInt(maxR - minR + 1);
        int g = minG + random.nextInt(maxG - minG + 1);
        int b = minB + random.nextInt(maxB - minB + 1);

        // Trả về đối tượng Color
        return Color.rgb(r, g, b);
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
}
