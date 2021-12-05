package com.example.groceryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    EditText name, email, pass1, pass2;
    TextView err;
    Button signup, login;

    FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        err = findViewById(R.id.errSignup);
        err.setText("");
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        name = findViewById(R.id.nameSignup);
        email = findViewById(R.id.emailSignup);
        pass1 = findViewById(R.id.pass1Signup);
        pass2 = findViewById(R.id.pass2Signup);

        name.setText("");
        email.setText("");
        pass1.setText("");
        pass2.setText("");

        login = findViewById(R.id.switchLogin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchLogin();
            }
        });

        signup = findViewById(R.id.signupBtn);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                err.setTextColor(Color.RED);
                String nameIn = name.getText().toString();
                String emailIn = email.getText().toString();
                String pass1In = pass1.getText().toString();
                String pass2In = pass2.getText().toString();

                boolean noerrs = true;

                if (TextUtils.isEmpty((nameIn))) {name.setError("Name is required. Please fill in."); noerrs = false;};
                if (TextUtils.isEmpty((emailIn))) {email.setError("Email is required. Please fill in."); noerrs = false;};
                if (TextUtils.isEmpty((pass1In))) {pass1.setError("Password is required. Please fill in."); noerrs = false;};
                if (!pass2In.equals(pass1In)) {pass2.setError("Passwords do not match. Please recheck."); noerrs = false;};


                if (noerrs) {

                    mAuth.createUserWithEmailAndPassword(emailIn, pass1In).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            FirebaseUser current = authResult.getUser();
                            if (current != null) {
                                String UID = current.getUid();
                                db.collection("Customers").document(UID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        if (documentSnapshot.exists()) {
                                            err.setTextColor(Color.BLUE);
                                            err.setText("Account exists. Click here to login");
                                            err.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    name.setText("");
                                                    email.setText("");
                                                    pass1.setText("");
                                                    pass2.setText("");
                                                    switchLogin();
                                                }
                                            });
                                        } else {
                                            Map<String, Object> data = new HashMap<>();
                                            data.put("Name", nameIn);
                                            data.put("Email", emailIn);
                                            data.put("UID", UID);
                                            data.put("Orders", Arrays.asList());
                                            db.collection("Customers").document(UID).set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    name.setText("");
                                                    email.setText("");
                                                    pass1.setText("");
                                                    pass2.setText("");
                                                    goToUserView();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    err.setText(e.getMessage());
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
                            } else {
                                err.setText("Something went wrong. Please try again.");
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
//                            mAuth.signOut();
                            err.setText(e.getMessage());
                        }
                    });
                }
            }
        });

    }

    //view changers

    public void goToUserView() {
        Intent intent = new Intent(this, CustomerNav.class);
        startActivity(intent);
    }

    public void switchLogin() {
        Intent intent = new Intent(this, ShowLoginActivity.class);
        startActivity(intent);
    }

}