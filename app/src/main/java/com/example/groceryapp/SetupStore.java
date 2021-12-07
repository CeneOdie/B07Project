package com.example.groceryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.groceryapp.MainActivity;
import com.example.groceryapp.StoreOwner;
import com.example.groceryapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

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


        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.main_navig);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(3);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){

                    case R.id.nav_stores:
                        Intent intent = new Intent(SetupStore.this, StoreList.class);
                        intent.putExtra("account", "Customer");
                        intent.putExtra("auth", current);
                        startActivity(intent);
                        break;

                    case R.id.nav_cart:
//                        Intent intent2 = new Intent(StoreList.this, CartActivity.class);
//                        startActivity(intent2);
                        break;



                    case R.id.nav_cust_history:
//                        Intent intent3 = new Intent(StoreList.this, CustomerHistoryActivity.class);
//                        startActivity(intent3);
                        break;


                }

                return false;
            }
        });


        create.setOnClickListener(view -> {
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

                    db.collection("Store Owners").document(current.getUid()).get().addOnSuccessListener(documentSnapshot -> {
                        StoreOwner store = documentSnapshot.toObject(StoreOwner.class);
                        if (store != null) {
                            Toast.makeText(context, "Store account already set up as " + documentSnapshot.getString("Store Name") + ". Redirecting...", Toast.LENGTH_SHORT).show();
                            goToStoreView();
                        } else {
                            setStoreData();
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
        });

    }


    public void goHome() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


    public void setStoreData() {
        StoreOwner store = new StoreOwner(current.getUid(),
                                        name.getText().toString().trim(),
                                        address.getText().toString().trim(),
                                        current.getDisplayName(),
                                        current.getEmail(),
                                         new ArrayList<DocumentReference>(),new ArrayList<DocumentReference>());
        db.collection("Store Owners").document(current.getUid()).set(store).addOnSuccessListener(unused -> {
            Toast.makeText(context, "Welcome to your Store Account! Get started by adding some products to your store.", Toast.LENGTH_LONG).show();
            goToStoreView();
        }).addOnFailureListener(e -> {
            // store create failed, try again
            Toast.makeText(context, "Store create failed. Please try again.", Toast.LENGTH_SHORT).show();
        });
    }

    public void goToStoreView() {
        Intent intent = new Intent(this, listStoreOrders.class);
        intent.putExtra("account", "Store");
        intent.putExtra("auth", current);
        startActivity(intent);
    }


}