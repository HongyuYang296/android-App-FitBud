package com.example.assignment3.ui.report;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PieChartViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public PieChartViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Pie chart fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}