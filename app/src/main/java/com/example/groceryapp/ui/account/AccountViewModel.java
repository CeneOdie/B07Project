package com.example.groceryapp.ui.account;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AccountViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AccountViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Switch between account types (or setup store). Logout. Delete account");


    }



    public LiveData<String> getText() {
        return mText;
    }
}