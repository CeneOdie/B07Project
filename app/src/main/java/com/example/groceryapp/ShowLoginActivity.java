package com.example.groceryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
    Switch accType;
    FirebaseAuth mAuth;
    Button login;
    FirebaseFirestore db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_login);
        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.emailLogin);
        pass = findViewById(R.id.passLogin);
        login = findViewById(R.id.loginBtn);
        accType = findViewById(R.id.loginType);
        err = findViewById(R.id.errLogin);
        err.setText("");

        db = FirebaseFirestore.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                err.setText("");
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
                    if (accType.isChecked()) loginStore(emailIn, passIn);
                    else loginCust(emailIn, passIn);
                }
            }
        });


    }


    public void goToCustView() {
        Intent intent = new Intent(this, StoreList.class);
        startActivity(intent);
    }

    public void goToStoreView() {
        Intent intent = new Intent(this, listStoreOrders.class);
        startActivity(intent);
    }

    public void loginCust(String email, String pass) {
        login(email, pass);
        FirebaseUser current = mAuth.getCurrentUser();
        db.collection("Customers").whereEqualTo("UID", current.getUid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                goToCustView();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                err.setText("Customer account not set up. Do you want to set one up now? Click Here.");

                err.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        err.setText("Creating customer account");
                    }
                });
            }
        });

    }

    public void loginStore(String email, String pass) {
        login(email, pass);
        err.setText("store not set up");

    }

    public void login(String email, String pass) {
        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                FirebaseUser current = mAuth.getCurrentUser();
//                err.setText(current.getEmail());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                err.setText(e.getMessage());
            }
        });
    }


    public void switchSignup(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

}