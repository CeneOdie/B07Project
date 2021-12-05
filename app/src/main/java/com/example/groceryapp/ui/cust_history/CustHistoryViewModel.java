package com.example.groceryapp.ui.cust_history;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CustHistoryViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public CustHistoryViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Show history of placed orders.");
    }

    public LiveData<String> getText() {
        return mText;
    }
}