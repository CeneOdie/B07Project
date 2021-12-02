package com.example.groceryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class showCart extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_cart);
    }

    //TODO Perry show the open order for a customer here (Do we want to allow customer to have only ONE open order at a time? to make things easier?)
}