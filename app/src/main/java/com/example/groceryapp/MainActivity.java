package com.example.groceryapp;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

//    Button login;
//    Button signup;
//
//    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        mAuth = FirebaseAuth.getInstance();


//        login = root

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