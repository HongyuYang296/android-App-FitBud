package com.example.assignment3.ui.report;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BarChartViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public BarChartViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Generate report fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}