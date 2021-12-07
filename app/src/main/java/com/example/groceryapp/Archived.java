package com.example.groceryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class Archived extends AppCompatActivity {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    AdapterOrder adapter;

    FirebaseUser current;
    String account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archived);

        Bundle extras = getIntent().getExtras();
        current = (FirebaseUser) extras.get("auth");
        account = extras.getString("account");


        //Hide default toolbar and its title
        // Display custom toolbar
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        setSupportActionBar(findViewById(R.id.archivedTB));

        // Return to listStoreOrders activity
        FloatingActionButton btnBack = findViewById(R.id.backbtn);
        btnBack.setOnClickListener(v -> startActivity(new Intent(Archived.this, listStoreOrders.class)));
        setupRecyclerView();



        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.main_navig);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.nav_orders:
                        Intent intent = new Intent(Archived.this, listStoreOrders.class);
                        intent.putExtra("account", "store");
                        intent.putExtra("auth", current);
                        startActivity(intent);
                        break;

                    case R.id.nav_products:
                        Intent intent2 = new Intent(Archived.this, ListProducts.class);
                        intent2.putExtra("account", "Store");
                        intent2.putExtra("auth", current);
                        startActivity(intent2);
                        break;


                    case R.id.nav_history:
//                        Intent intent3 = new Intent(Archived.this, Archived.class);
//                        intent3.putExtra("account", "Store");
//                        intent3.putExtra("auth", current);
//                        startActivity(intent3);
                        break;




                }
                return false;
            }
        });


    }

    public void setupRecyclerView() {

        Query query = db.collection("Orders")
                .orderBy("DateTime", Query.Direction.ASCENDING)
                .whereEqualTo("Archived", true);

        FirestoreRecyclerOptions<Order> options = new FirestoreRecyclerOptions.Builder<Order>()
                .setQuery(query, Order.class).build();

        adapter = new AdapterOrder(options);
        RecyclerView recyclerView = findViewById(R.id.archivedRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Swipe to delete order from activity and database
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                adapter.deleteOrder(viewHolder.getBindingAdapterPosition());
            }
        }).attachToRecyclerView(recyclerView);
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