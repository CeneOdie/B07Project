package com.example.b07project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ShowLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_login);
    }

    public void showSignUp(View view){
        Intent intent = new Intent(this, ShowSignUpActivity.class);
        startActivity(intent);
    }
}