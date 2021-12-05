package com.example.groceryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ShowLoginActivity extends AppCompatActivity {

    EditText email, pass;
    TextView err;
    FirebaseAuth mAuth;
    Button login, signup;
    FirebaseFirestore db;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_login);
        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.emailLogin);
        pass = findViewById(R.id.passLogin);
        login = findViewById(R.id.loginBtn);
        err = findViewById(R.id.errLogin);
        err.setText("");

        email.setText("");
        pass.setText("");

        db = FirebaseFirestore.getInstance();

        signup = findViewById(R.id.switchSignUp);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email.setText("");
                pass.setText("");
                switchSignup();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                    mAuth.signInWithEmailAndPassword(emailIn, passIn).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            FirebaseUser current = authResult.getUser();
                            if (current != null ) {
                                email.setText("");
                                pass.setText("");
                                goToUserView();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mAuth.signOut();
                            err.setText(e.getMessage());
                        }
                    });
                }
            }
        });
    }

    public void switchSignup() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    public void goToUserView() {
        Intent intent = new Intent(this, CustomerNav.class);

        intent.putExtra("logged in", mAuth.getCurrentUser());
        startActivity(intent);
    }

}