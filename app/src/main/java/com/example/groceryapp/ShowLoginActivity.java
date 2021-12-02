package com.example.groceryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ShowLoginActivity extends AppCompatActivity {

    EditText email, pass;
    TextView err;
    Switch custType;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_login);

        mAuth = FirebaseAuth.getInstance();
        err = findViewById(R.id.errLogin);
        err.setText("");
    }

    public void switchSignup(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

//    public void changeAccType(View view) {
//        custType = findViewById(R.id.loginType);
//        if (custType.isChecked()) {
//            custType.setText("Store");
//        }
//        else {
//            custType.setText("Customer");
//        }
//    }

    public void login(View view) {
        email = findViewById(R.id.emailLogin);
        pass = findViewById(R.id.passLogin);
        err = findViewById(R.id.errLogin);
        err.setText("");
        if (email.getText().toString().length() != 0 || pass.getText().toString().length() != 0) {
            mAuth.signInWithEmailAndPassword(email.getText().toString(), pass.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = mAuth.getCurrentUser();

//                                err.setText("Logged in" + mAuth.getCurrentUser().getEmail().toString());

                                if (custType.isChecked()) {
                                    // TODO go to store home
                                } else {
                                    //TODO go to customer home
                                }

                            } else {
                                // If sign in fails, display a message to the user.
//                                Log.w(TAG, "signInWithEmail:failure", task.getException());
//                                Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
//                                        Toast.LENGTH_SHORT).show();
//                                updateUI(null);
//                                err.setText("Login failed. Consider signing up instead.");
                                err.setText(task.getException().toString());
                            }

                        }
                    });
        } else err.setText("Please fill in all the fields");

    }

}