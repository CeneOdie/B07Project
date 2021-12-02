package com.example.groceryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignUpActivity extends AppCompatActivity {

    EditText name, email, pass1, pass2, store, address;
    TextView err;
    Switch acctype;

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
    }

    public void switchLogin(View view) {
        Intent intent = new Intent(this, ShowLoginActivity.class);
        startActivity(intent);
    }

    public void signUp(View view) {
        err = findViewById(R.id.errSignup);
        name = findViewById(R.id.nameSignup);
        email = findViewById(R.id.emailSignup);
        pass1 = findViewById(R.id.pass1Signup);
        pass2 = findViewById(R.id.pass2Signup);
        store = findViewById(R.id.storeSignup);
        address = findViewById(R.id.addressSignup);
        acctype = findViewById(R.id.acctypeSignup);

        if (name.getText().toString().length() != 0 || email.getText().toString().length() != 0 || pass1.getText().toString().length() != 0 || pass2.getText().toString().length() != 0) {
            err.setText("Please fill in all the required fields");
        } else {
            if (pass1.getText().toString() != pass2.getText().toString()) {
                err.setText("Passwords do not match");
            }

            if (acctype.isChecked() ){
                //TODO store signup
            } else {
                //cust signup
                mAuth.createUserWithEmailAndPassword(email.getText().toString(), pass1.getText().toString())
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
//                                    Log.d(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(name.getText().toString())
//                                            .setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg"))
                                            .build();
                                    String id = user.getUid();


                                    db.collection("Customers").add();

//                                    updateUI(user);
                                } else {
                                    err.setText(task.getException().getLocalizedMessage());
                                    // If sign in fails, display a message to the user.
//                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
//                                    Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
//                                            Toast.LENGTH_SHORT).show();
//                                    updateUI(null);
                                }
                            }
                        });
            }

        }

    }
}