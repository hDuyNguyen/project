package com.example.powertrackingapp.view;

import static com.example.powertrackingapp.AppConstant.DEVICE_ID;
import static com.example.powertrackingapp.AppConstant.DEVICE_NAME;
import static com.example.powertrackingapp.AppConstant.TAG;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import com.example.powertrackingapp.SharedPreferencesHelper;
import com.example.powertrackingapp.Utils;
import com.example.powertrackingapp.controller.Usecase;
import com.example.powertrackingapp.databinding.HomeBinding;
import com.example.powertrackingapp.model.PowerConsumption;
import com.example.powertrackingapp.model.User;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class HomeFragment extends Fragment {
    HomeBinding binding;
    User user;
    Usecase usecase = Usecase.getInstance();
    List<String> dayList = new ArrayList<>();
    List<Float> powerList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = HomeBinding.inflate(inflater, container, false);
        setBackGroundButton();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getUserInfo();

        binding.button1.setOnClickListener(v -> {
            sendFullScreenNotification();
        });

        PowerConsumption powerConsumption = new PowerConsumption();
        powerConsumption.setToken(user.getToken());
        powerConsumption.setDeviceName(DEVICE_NAME);
        powerConsumption.setRealDeviceId(DEVICE_ID);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDate endDate = LocalDate.now();
            LocalDate startDate = endDate.minusMonths(5);
            powerConsumption.setEndDate(endDate);
            powerConsumption.setStartDate(startDate);
        }

        try {
            String result = usecase.getPowerConsumption(powerConsumption);
            Utils.convertJsonToArrayPowerConsumption(dayList, powerList, result);
            Log.i(TAG, "result: " + result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void sendFullScreenNotification() {
        String channelId = "full_screen_channel";

        // Đặt âm thanh thông báo (âm thanh mặc định hoặc tùy chỉnh)
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

        // Tạo Intent mở AlertActivity
        Intent intent = new Intent(getActivity(), AlertActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                requireContext(),
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        startActivity(intent);

        // Lấy NotificationManager từ context của Fragment
        NotificationManager notificationManager = (NotificationManager) requireContext().getSystemService(Context.NOTIFICATION_SERVICE);

        // Tạo Notification Channel (chỉ cần cho Android 8.0 trở lên)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Cảnh báo toàn màn hình",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);
            channel.setSound(soundUri, null); // Gán âm thanh cho kênh
            channel.enableVibration(true);   // Bật rung
            channel.setVibrationPattern(new long[]{0, 500, 1000, 500}); // Mẫu rung: chờ, rung, chờ, rung
            notificationManager.createNotificationChannel(channel);
        }

        // Xây dựng thông báo
        NotificationCompat.Builder builder = new NotificationCompat.Builder(requireContext(), channelId)
                .setSmallIcon(android.R.drawable.ic_dialog_alert)
                .setContentTitle("Cảnh báo khẩn cấp")
                .setContentText("Phát hiện bất thường thiết bị! Kiểm tra ngay.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setSound(soundUri) // Gán âm thanh cho thông báo
                .setVibrate(new long[]{0, 500, 1000, 500}) // Mẫu rung
                .setFullScreenIntent(pendingIntent, true)
                .setAutoCancel(true);

        // Gửi thông báo
        notificationManager.notify(1, builder.build());
        Log.i(TAG, "Full screen notification sent successfully");


        // Gửi rung (tùy chỉnh rung thêm nếu cần)
        Vibrator vibrator = (Vibrator) requireContext().getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createWaveform(new long[]{0, 500, 1000, 500}, -1));
            } else {
                vibrator.vibrate(new long[]{0, 500, 1000, 500}, -1);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("duynm", "onResume: ");
        getUserInfo();
        createChart();
    }

    private void getUserInfo() {
        if (SharedPreferencesHelper.isLoggedIn(requireContext())) {
            user = SharedPreferencesHelper.getUser(requireContext());
            if (user != null) {
                binding.userName.setText(user.getFullName());
                binding.userAddress.setText(user.getAddress());
            }
        }
    }

    private void createChart() {
        BarChart barChart = binding.chart;
        barChart.getAxisRight().setDrawLabels(false);

        List<Integer> colors = new ArrayList<>();
        ArrayList<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < powerList.size() - 1; i++) {
            entries.add(new BarEntry(i, powerList.get(i)));
            colors.add(getRandomColor());
        }

        YAxis yAxis = barChart.getAxisLeft();
        yAxis.setAxisMaximum(0f);
        yAxis.setAxisMaximum(70000f);
        yAxis.setAxisLineWidth(2f);
        yAxis.setAxisLineColor(Color.BLACK);
        yAxis.setLabelCount(20);

        BarDataSet barDataSet = new BarDataSet(entries, "Type");
        barDataSet.setColors(colors);

        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);

        barChart.getDescription().setEnabled(false);
        barChart.invalidate();

        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(dayList));
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getXAxis().setAxisLineColor(Color.BLACK);
        barChart.getXAxis().setGranularity(1f);
        barChart.getAxisLeft().setDrawLabels(false);
        barChart.getXAxis().setGranularityEnabled(true);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.setScaleEnabled(false);
        barChart.getLegend().setEnabled(false);
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

    private void setBackGroundButton() {
        Button button1 = binding.button1;
        button1.setBackgroundTintList(null);

        Button button2 = binding.button2;
        button2.setBackgroundTintList(null);

        Button button3 = binding.button3;
        button3.setBackgroundTintList(null);
    }

}
