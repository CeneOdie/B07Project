package com.example.groceryapp.ui.storeHistory;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class StoreHistoryViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public StoreHistoryViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Show history of stores completed orders");
    }

    public LiveData<String> getText() {
        return mText;
    }
}