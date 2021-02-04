package com.example.afinal.ui.home;

import android.database.SQLException;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.afinal.DatabaseHelper;

import java.io.IOException;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is users fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}