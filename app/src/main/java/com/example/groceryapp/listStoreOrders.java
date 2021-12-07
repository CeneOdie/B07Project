package com.example.groceryapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class listStoreOrders extends AppCompatActivity implements AdapterOrder.OnOrderClickListener {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private AdapterOrderMain adapter;

    FirebaseUser current;
    String account;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_store_orders);


        Bundle extras = getIntent().getExtras();
        current = (FirebaseUser) extras.get("auth");
        account = extras.getString("account");



        // Hide default toolbar and its title & display custom toolbar
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        setSupportActionBar(findViewById(R.id.archivedTB));

        // Return to ListProducts activity
        FloatingActionButton btnBack = findViewById(R.id.backbtn);
        btnBack.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), ListProducts.class)));

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uID = null;
        if (user != null) {
            // How can I check if user is a store owner?
            uID = user.getUid();
        }

        Query query = db.collection("Orders")
                .whereEqualTo("Store", "/Store Owners/" + uID)
                .orderBy("DateTime", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<Order> options = new FirestoreRecyclerOptions.Builder<Order>()
                .setQuery(query, Order.class)
                .build();
        adapter = new AdapterOrderMain(options);

        RecyclerView recyclerView = findViewById(R.id.ordersRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.setListener(this);



        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.main_navig);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.nav_orders:
//                        Intent intent = new Intent(listStoreOrders.this, listStoreOrders.class);
//                        intent.putExtra("account", "store");
//                        intent.putExtra("auth", current);
//                        startActivity(intent);
                        break;

                    case R.id.nav_products:
                        Intent intent2 = new Intent(listStoreOrders.this, ListProducts.class);
                        intent2.putExtra("account", "Store");
                        intent2.putExtra("auth", current);
                        startActivity(intent2);
                        break;


                    case R.id.nav_history:
                        Intent intent3 = new Intent(listStoreOrders.this, Archived.class);
                        intent3.putExtra("account", "Store");
                        intent3.putExtra("auth", current);
                        startActivity(intent3);
                        break;


                    case R.id.nav_store_account:
                        Intent intent4 = new Intent(listStoreOrders.this, StoreAccountActivity.class);
                        intent4.putExtra("account", "Store");
                        intent4.putExtra("auth", current);
                        startActivity(intent4);
                        break;

                }
                return false;
            }
        });


    }

    @Override
    public void onOrderClick(DocumentSnapshot snapshot, int position) {
        String id = snapshot.getId();
        Intent intent = new Intent(getApplicationContext(), ViewOrderDetail.class);
        intent.putExtra("documentId", id);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}