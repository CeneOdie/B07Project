package com.example.groceryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class listOrders extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_orders);
    }

    //TODO Perry list of orders for a customer (allow for a field of completed/notcompleted when created to use in db query)
}