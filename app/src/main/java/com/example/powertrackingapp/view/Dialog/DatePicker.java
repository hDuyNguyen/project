package com.example.powertrackingapp.view.Dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;

public class DatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private static final String ARG_VIEW_ID = "view_id";
    public static DatePicker newInstance(int viewId) {
        DatePicker datePicker = new DatePicker();
        Bundle arg = new Bundle();
        arg.putInt(ARG_VIEW_ID, viewId);
        datePicker.setArguments(arg);
        return datePicker;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(requireContext(),
                this,
                year, month, dayOfMonth);
    }

    @Override
    public void onDateSet(android.widget.DatePicker datePicker, int year, int month, int dayOfMonth) {
        int viewId = requireArguments().getInt(ARG_VIEW_ID);
        if (getParentFragment() instanceof DatePickerListener) {
            ((DatePickerListener) getParentFragment()).onDateSet(viewId, year, month, dayOfMonth);
        }
    }

    public interface DatePickerListener {
        void onDateSet(int viewId, int year, int month, int dayOfMonth);
    }

    public static String formatDate(int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

    public static LocalDate convertStringToLocalDate(String date) {
        LocalDate finalDate = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate parsedDate = LocalDate.parse(date, formatter);

            // Lấy giá trị năm, tháng, ngày
            int year = parsedDate.getYear();
            int month = parsedDate.getMonthValue();
            int day = parsedDate.getDayOfMonth();

            // Tạo LocalDate bằng LocalDate.of()
            finalDate = LocalDate.of(year, month, day);
        }
        return finalDate;
    }
}
