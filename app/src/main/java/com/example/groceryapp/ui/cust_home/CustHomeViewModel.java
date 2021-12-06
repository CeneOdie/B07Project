package com.example.groceryapp.ui.cust_home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.groceryapp.StoreOwner;

import java.util.List;

public class CustHomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<List<StoreOwner>> stores;

    public CustHomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Show list of stores");
    }

    public LiveData<String> getText() {
        return mText;
    }
}