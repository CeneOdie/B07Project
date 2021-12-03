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
                            FirebaseUser current = mAuth.getCurrentUser();

                            if (current != null ) {
                                if (accType.isChecked()) loginStore(current.getUid());
                                else loginCust(current.getUid());
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

    public void loginCust(String UID) {
        db.collection("Customers").document(UID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) goToCustView();
                else {
                    err.setText("Customer account not created. Click here to create one quickly.");
                    err.setTextColor(Color.BLUE);
                    err.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //create customer
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                err.setText("Something went wrong. Please try again.");
            }
        });
    }

    public void loginStore(String UID) {
        db.collection("Store Owners").document(UID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) goToStoreView();
                else {
                    err.setTextColor(Color.BLUE);
                    err.setText("Store account not created. Click here to create one.");
                    err.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            switchSignup();
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                err.setText("Something went wrong. Please try again.");
            }
        });
    }


    //View changers

    public void goToCustView() {
        Intent intent = new Intent(this, StoreList.class);
        startActivity(intent);
    }

    public void goToStoreView() {
        Intent intent = new Intent(this, listStoreOrders.class);
        startActivity(intent);
    }

    public void switchSignup() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

}