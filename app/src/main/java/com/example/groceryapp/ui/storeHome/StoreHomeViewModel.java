package com.example.groceryapp.ui.storeHome;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class StoreHomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public StoreHomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Show list of store's open orders.");
    }

    public LiveData<String> getText() {
        return mText;
    }
}