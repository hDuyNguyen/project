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
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class DetailDeviceFragment extends Fragment implements DatePicker.DatePickerListener {
    DetailDeviceBinding binding;
    DatePickerModel datePickerModel;
    DatePickerController datePickerController;

    private final List<String> value = Arrays.asList("27/11", "28/11", "29/11");

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

        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, 80f));
        entries.add(new BarEntry(1, 10f));
        entries.add(new BarEntry(2, 50f));

        YAxis yAxis = barChart.getAxisLeft();
        yAxis.setAxisMaximum(0f);
        yAxis.setAxisMaximum(100f);
        yAxis.setAxisLineWidth(2f);
        yAxis.setAxisLineColor(Color.BLACK);
        yAxis.setLabelCount(20);

        BarDataSet barDataSet = new BarDataSet(entries, "Type");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);

        barChart.getDescription().setEnabled(false);
        barChart.invalidate();

        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(value));
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
