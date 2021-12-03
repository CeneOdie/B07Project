package com.example.groceryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignUpActivity extends AppCompatActivity {

    EditText name, email, pass1, pass2, storename, address;
    TextView err;
    Switch acctype;
    Button signup;

    FirebaseAuth mAuth;
    FirebaseFirestore db;

    User user;


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
        storename = findViewById(R.id.storeSignup);
        address = findViewById(R.id.addressSignup);
        acctype = findViewById(R.id.acctypeSignup);

        signup = findViewById(R.id.signupBtn);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameIn = name.getText().toString();
                String emailIn = email.getText().toString();
                String pass1In = pass1.getText().toString();
                String pass2In = pass2.getText().toString();
                String storeIn = storename.getText().toString();
                String addrIn = address.getText().toString();

                boolean noerrs = true;

                if (TextUtils.isEmpty((nameIn))) {name.setError("Name is required. Please fill in."); noerrs = false;};
                if (TextUtils.isEmpty((emailIn))) {email.setError("Email is required. Please fill in."); noerrs = false;};
                if (TextUtils.isEmpty((pass1In))) {pass1.setError("Password is required. Please fill in."); noerrs = false;};
                if (!pass2In.equals(pass1In)) {pass2.setError("Passwords do not match. Please recheck."); noerrs = false;};

                if (acctype.isChecked() && TextUtils.isEmpty(storeIn)) {
                    storename.setError("Store name is required, or change to customer account."); noerrs = false;};
                if (acctype.isChecked() && TextUtils.isEmpty(addrIn)) {address.setError("Store address is required, or change to customer account."); noerrs = false;};

                if (noerrs) {
                    if (acctype.isChecked()) signUp(nameIn, emailIn, pass1In, storeIn, addrIn);
                    else signUp(nameIn, emailIn, pass1In);
                }

            }
        });

        acctype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (acctype.isChecked()){
                    storename.setVisibility(View.VISIBLE);
                    address.setVisibility(View.VISIBLE);
                } else {
                    storename.setVisibility(View.GONE);
                    address.setVisibility(View.GONE);

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

    public void signUp(String name, String email, String pass, String storename, String addr) {
        createUser(email, pass, name);
        FirebaseUser current = mAuth.getCurrentUser();
        user = new User(current.getDisplayName(), current.getEmail(), current.getUid());
        if (current != null) {
            User user = new User(name, email, current.getUid());
            String storeid = user.getStore();
            if (!TextUtils.isEmpty(storeid)) {
                mAuth.signOut();
                err.setText("Store account already exists. Log in to access.");
            } else {
                StoreOwner store = new StoreOwner(user.name, user.email, user.UID, storename, addr);
                goToStoreView();
            }
        }
    }

    public void signUp(String name, String email, String pass) {
        createUser(email, pass, name);
        FirebaseUser current = mAuth.getCurrentUser();
        user = new User(current.getDisplayName(), current.getEmail(), current.getUid());
        if (current != null) {
            User user = new User(name, email, current.getUid());
            String custid = user.getCust();
            if (!TextUtils.isEmpty(custid)) {
                mAuth.signOut();
                err.setText("Customer account already exists. Log in to access.");
            } else {
                Customer cust = new Customer(user.name, user.email, user.UID);
                goToCustView();
            }
        }
    }

    public void createUser(String email, String pass, String name) {
        mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .build();
            }
        }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                err.setText(e.getMessage());
            }
        });
    }

    public void switchLogin(View view) {
        Intent intent = new Intent(this, ShowLoginActivity.class);
        startActivity(intent);
    }

}