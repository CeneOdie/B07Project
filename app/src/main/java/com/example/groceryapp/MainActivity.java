package com.example.groceryapp;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.groceryapp.Auth.ShowLoginActivity;
import com.example.groceryapp.Auth.SignUpActivity;

public class MainActivity extends AppCompatActivity {

    //Please dont change main activity. this routes to signup/login and from there will enter app as a customer of store owner
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void showLogin(View view) {
        Intent intent = new Intent(this, ShowLoginActivity.class);
        startActivity(intent);

    }

    public void showSignup(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);

    }


}