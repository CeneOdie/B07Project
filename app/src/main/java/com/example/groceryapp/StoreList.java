package com.example.groceryapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

public class StoreList extends AppCompatActivity {



    RecyclerView viewer;
    TextView err;
    ProgressBar progress;
    FirebaseFirestore db;
    FirebaseUser current;
    String account;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_list);
        Bundle extras = getIntent().getExtras();
        current = (FirebaseUser) extras.get("auth");
        account = (String) extras.get("account");

        db = FirebaseFirestore.getInstance();

            viewer = findViewById(R.id.viewStores);
            err = findViewById(R.id.text_cust_home);
            progress = findViewById(R.id.storesProgress);

            progress.setVisibility(View.VISIBLE);
            fetchStores();

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.main_navig);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.nav_stores:
//                        Intent intent = new Intent(StoreList.this, StoreList.class);
//                        intent.putExtra("account", "Customer");
//                        intent.putExtra("auth", current);
//                        startActivity(intent);
                        break;

                    case R.id.nav_cart:
                        Intent intent2 = new Intent(StoreList.this, CartActivity.class);
                        intent2.putExtra("account", "Customer");
                        intent2.putExtra("auth", current);
                        startActivity(intent2);
                        break;


                    case R.id.nav_cust_history:
                        Intent intent3 = new Intent(StoreList.this, CustomerHistoryActivity.class);
                        intent3.putExtra("account", "Customer");
                        intent3.putExtra("auth", current);
                        startActivity(intent3);
                        break;


                    case R.id.nav_account:
                        Intent intent4 = new Intent(StoreList.this, AccountActivity.class);
                        intent4.putExtra("account", "Customer");
                        intent4.putExtra("auth", current);
                        startActivity(intent4);
                        break;

                }
                return false;
            }
        });


        }

        public void showStores(ArrayList<StoreOwner> stores) {
            StoreAdapter adapter = new StoreAdapter(this, stores);
            viewer.setAdapter(adapter);

            viewer.setHasFixedSize(true);

            progress.setVisibility(View.GONE);
        }

        public void fetchStores() {

            ArrayList<StoreOwner> stores = new ArrayList<>();
            db = FirebaseFirestore.getInstance();
            stores.clear();
            db.collection("Store Owners").orderBy("Store Name", Query.Direction.ASCENDING)
                    .addSnapshotListener((value, error) -> {
                        if (error != null) {
                            err.setText(error.getMessage());
                            return;
                        }
                        if(value.isEmpty()) {
                            err.setText("No stores to display.Check back later.");
                        } else {
                            int noproducts = 0;
                            for(DocumentSnapshot doc : value.getDocuments()) {
                                StoreOwner newstore = doc.toObject(StoreOwner.class);
                                if (newstore != null) {
                                    if (newstore.getItems().isEmpty()) noproducts++;
                                    else {
                                        if (!stores.contains((newstore))) stores.add(newstore);
                                    }
                                }
                            }
//                            Toast.makeText(getActivity().getApplicationContext(), noproducts + " store(s) have no products", Toast.LENGTH_SHORT).show();

                            err.setText("Showing " + stores.size() + " stores (that have products)");
                            showStores(stores);
                        }
                    });
        }
    }

