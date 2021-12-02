package com.example.groceryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class listStoreOrders extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_store_orders);
    }

    //TODO Julia list of orders for a store (allow for a field of completed/notcompleted when created to use in db query)
}