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
import android.widget.ProgressBar;
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
    ProgressBar progress;

    FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        err = findViewById(R.id.errSignup);
        err.setText("");
        progress = findViewById(R.id.signupProgress);
        progress.setVisibility(View.GONE);
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
                progress.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);
                switchLogin();
            }
        });

        signup = findViewById(R.id.signupBtn);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progress.setVisibility(View.VISIBLE);

                err.setTextColor(Color.RED);
                String nameIn = name.getText().toString().trim();
                String emailIn = email.getText().toString().trim();
                String pass1In = pass1.getText().toString().trim();
                String pass2In = pass2.getText().toString().trim();

                boolean noerrs = true;

                if (TextUtils.isEmpty((nameIn))) {name.setError("Name is required. Please fill in."); noerrs = false;};
                if (TextUtils.isEmpty((emailIn))) {email.setError("Email is required. Please fill in."); noerrs = false;};
                if (TextUtils.isEmpty((pass1In))) {pass1.setError("Password is required. Please fill in."); noerrs = false;};
                if (!pass2In.equals(pass1In)) {pass2.setError("Passwords do not match. Please recheck."); noerrs = false;};


                if (noerrs) {

                    mAuth.createUserWithEmailAndPassword(emailIn, pass1In).addOnSuccessListener(new OnSuccessListener<AuthResult>() {

                        @Override
                        public void onSuccess(AuthResult authResult) {
                            // successfully created user
                            FirebaseUser current = authResult.getUser();

                            if (current != null) {

                                String UID = current.getUid();
                                UserProfileChangeRequest init = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(nameIn).build();
                                current.updateProfile(init).addOnSuccessListener(new OnSuccessListener<Void>() {

                                    @Override
                                    public void onSuccess(Void unused) {
                                        // successfully updated user profile
                                        db.collection("Customers").document(UID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {

                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                //successfully got customer doc


                                                if (documentSnapshot.exists()) {
                                                    //doc exists => abort
                                                    err.setTextColor(Color.BLUE);
                                                    progress.setVisibility(View.GONE);
                                                    err.setText("Account exists. Click here to login");

                                                    err.setOnClickListener(new View.OnClickListener() {

                                                        @Override
                                                        public void onClick(View view) {
                                                            progress.setVisibility(View.VISIBLE);
                                                            name.setText("");
                                                            email.setText("");
                                                            pass1.setText("");
                                                            pass2.setText("");
                                                            progress.setVisibility(View.GONE);
                                                            switchLogin();
                                                        }

                                                    });

                                                } else {
                                                    // doc dne, set with new data
                                                    Map<String, Object> data = new HashMap<>();
                                                    data.put("Name", nameIn);
                                                    data.put("Email", emailIn);
                                                    data.put("UID", UID);
                                                    data.put("Orders", Arrays.asList());

                                                    db.collection("Customers").document(UID).set(data).addOnSuccessListener(new OnSuccessListener<Void>() {

                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            // successfully set data
                                                            name.setText("");
                                                            email.setText("");
                                                            pass1.setText("");
                                                            pass2.setText("");
                                                            progress.setVisibility(View.GONE);
                                                            goToUserView();
                                                        }

                                                    }).addOnFailureListener(new OnFailureListener() {

                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            // failed to set data
                                                            progress.setVisibility(View.GONE);
                                                            err.setText(e.getMessage());
                                                        }

                                                    });

                                                }

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {

                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // failed to get customer doc
                                                progress.setVisibility(View.GONE);
                                                err.setText("Something went wrong. Please try again.");
                                            }

                                        });

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // failed to set user profile
                                        progress.setVisibility(View.GONE);
                                        err.setText(e.getMessage());
                                    }
                                });

                            } else {

                                // user was null
                                progress.setVisibility(View.GONE);
                                err.setText("Something went wrong. Please try again.");

                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {

                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // failed to create user
//                            mAuth.signOut();
                            progress.setVisibility(View.GONE);
                            err.setText(e.getMessage());
                        }

                    });
                } else {
                    progress.setVisibility(View.GONE);
                    err.setText("Please fill in all the required fields.");

                }
            }
        });

    }

    //view changers

    public void goToUserView() {
        Intent intent = new Intent(this, CustomerNav.class);
        intent.putExtra("auth", mAuth.getCurrentUser());
        intent.putExtra("account", "Customer");
        startActivity(intent);
    }

    public void switchLogin() {
        Intent intent = new Intent(this, ShowLoginActivity.class);
        startActivity(intent);
    }

}