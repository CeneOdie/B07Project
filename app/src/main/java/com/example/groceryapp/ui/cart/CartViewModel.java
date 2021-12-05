package com.example.groceryapp.ui.cart;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CartViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public CartViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("View cart (or show 'No Cart Open. Start shopping here' with link to home page.");
    }

    public LiveData<String> getText() {
        return mText;
    }
}