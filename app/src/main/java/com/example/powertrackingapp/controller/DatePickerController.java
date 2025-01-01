package com.example.powertrackingapp.controller;

import com.example.powertrackingapp.model.DatePickerModel;

public class DatePickerController {
    private static DatePickerController instance;
    private final DatePickerModel datePickerModel;

    public static DatePickerController getInstance(DatePickerModel datePickerModel) {
        if (instance == null) {
            instance = new DatePickerController(datePickerModel);
        }
        return instance;
    }

    public DatePickerController(DatePickerModel datePickerModel) {
        this.datePickerModel = datePickerModel;
    }

    public void setStartDate(String date) {
        datePickerModel.setStartDate(date);
    }

    public void setEndDate(String date) {
        datePickerModel.setEndDate(date);
    }

    public String getStartDate() {
        return datePickerModel.getStartDate();
    }

    public String getEndDate() {
        return datePickerModel.getEndDate();
    }
}
