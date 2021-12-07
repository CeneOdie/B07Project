package com.example.groceryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class ShowLoginActivity extends AppCompatActivity {

    EditText email, pass;
    TextView err, forgot;
    FirebaseAuth mAuth;
    Button login;
    FirebaseFirestore db;
    ProgressBar progress;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_login);
        progress = findViewById(R.id.loginProgress);
        progress.setVisibility(View.GONE);
        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.emailLogin);
        pass = findViewById(R.id.passLogin);
        login = findViewById(R.id.loginBtn);
        err = findViewById(R.id.errLogin);
        err.setText("");

        email.setText("");
        pass.setText("");

        db = FirebaseFirestore.getInstance();

        forgot = findViewById(R.id.forgotPass);
        forgot.setOnClickListener(view -> {
            progress.setVisibility(View.VISIBLE);

            String emailIn = email.getText().toString().trim();
            if (TextUtils.isEmpty(emailIn)) email.setError("Please enter a recovery email.");
            else {
                mAuth.sendPasswordResetEmail(emailIn).addOnSuccessListener(unused -> {
                    progress.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Password reset email sent. Please check your mailbox", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(e -> {
                    progress.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Unable to send password reset email. Please check details and try again, or sign up instead.", Toast.LENGTH_SHORT).show();
                });
            }
        });


        login.setOnClickListener(v -> {

            progress.setVisibility(View.VISIBLE);
            err.setText("");
            err.setTextColor(Color.RED);
            String emailIn = email.getText().toString().trim();
            String passIn = pass.getText().toString().trim();
            boolean noerrs = true;

            if (TextUtils.isEmpty(emailIn)) {
                email.setError("Please fill in an email address");
                noerrs = false;
            }

            if (TextUtils.isEmpty(passIn)) {
                pass.setError("Please fill in a password");
                noerrs = false;
            }

            if (noerrs) {
                mAuth.signInWithEmailAndPassword(emailIn, passIn).addOnSuccessListener(authResult -> {
                    FirebaseUser current = authResult.getUser();
                    if (current != null ) {
                        email.setText("");
                        pass.setText("");
                        progress.setVisibility(View.GONE);
                        goToUserView();
                    }
                }).addOnFailureListener(e -> {
                    mAuth.signOut();
                    progress.setVisibility(View.GONE);
                    err.setText(e.getMessage());
                });
            } else {
                progress.setVisibility(View.GONE);
                err.setText("Please fill in all required fields.");
            }

        });



        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.main_navig);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){

                    case R.id.nav_login:

                        break;

                    case R.id.nav_main_home:
                        Intent intent1 = new Intent(ShowLoginActivity.this, MainActivity.class);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent1);
                        break;



                    case R.id.nav_signup:
                        Intent intent2 = new Intent(ShowLoginActivity.this, SignUpActivity.class);
                        startActivity(intent2);
                        break;

                }

                return false;
            }
        });
    }

    public void goToUserView() {
        Intent intent = new Intent(this, StoreList.class);

        intent.putExtra("auth", mAuth.getCurrentUser());
        intent.putExtra("account", "Customer");
        startActivity(intent);
    }

}