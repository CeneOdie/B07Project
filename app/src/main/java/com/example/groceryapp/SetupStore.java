package com.example.groceryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SetupStore extends AppCompatActivity {

    Button create;
    EditText name, address;
    TextView title;

    FirebaseUser current;
    FirebaseFirestore db;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_store);

        create = findViewById(R.id.storeSetupCreate);
        name = findViewById(R.id.storeSetupName);
        address = findViewById(R.id.storeSetupAddress);
        title = findViewById(R.id.setupStoreTitle);

        current = (FirebaseUser) getIntent().getExtras().get("auth");
        db = FirebaseFirestore.getInstance();
        context = getApplicationContext();

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean noerrs = true;

                if (TextUtils.isEmpty(name.getText().toString().trim())) {
                    name.setError("Store name is required");
                    noerrs = false;
                }
                if (TextUtils.isEmpty(address.getText().toString().trim())) {
                    address.setError("Store address is required");
                    noerrs = false;
                }

                if (noerrs) {

                    if (current == null) {
                        // not logged in, please log in and try again

                        Toast.makeText(context, "No user logged in. Redirecting...", Toast.LENGTH_SHORT).show();
                        goHome();
                    } else {
                        //recheck if store exists
                        db.collection("Store Owners").document(current.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()) {
                                    Toast.makeText(context, "Store account already set up as " + documentSnapshot.getString("Store Name") + ". Redirecting...", Toast.LENGTH_SHORT).show();
                                    goToStoreView();
                                } else {
                                    setStoreData();
                                }

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // user does not have store. create new
                                setStoreData();
                            }
                        });
                    }
                }
            }
        });

    }


    public void goHome() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


    public void setStoreData() {
        Map<String, Object> data = new HashMap<>();
        data.put("Name", current.getDisplayName());
        data.put("Email", current.getEmail());
        data.put("UID", current.getUid());
        data.put("Orders", Arrays.asList());
        data.put("Items", Arrays.asList());
        data.put("Store Name", name.getText().toString().trim());
        data.put("Address", address.getText().toString().trim());
        db.collection("Store Owners").document(current.getUid()).set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(context, "Welcome to your Store Account! Get started by adding some items to your store.", Toast.LENGTH_LONG).show();
                goToStoreView();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // store create failed, try again
                Toast.makeText(context, "Store create failed. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void goToStoreView() {
        Intent intent = new Intent(this, StoreNav.class);
        intent.putExtra("account", "Store");
        intent.putExtra("auth", current);
        startActivity(intent);
    }


}