package com.example.groceryapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ListProducts extends AppCompatActivity {

    private AdapterProduct adapter;

    FirebaseUser current;
    String account;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_products);

        Bundle extras = getIntent().getExtras();
        current = (FirebaseUser) extras.get("auth");
        account = extras.getString("account");


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        RecyclerView recyclerView = findViewById(R.id.productsRecyclerView);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uID = null;
        if (user != null) {
            uID = user.getUid();
        }

        Query query = db.collection("Items")
                .whereEqualTo("Store", "/Store Owners/" + uID)
                .orderBy("Name", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Product> options = new FirestoreRecyclerOptions.Builder<Product>()
                .setQuery(query, Product.class)
                .build();

        adapter = new AdapterProduct(options);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                adapter.deleteItem(viewHolder.getBindingAdapterPosition());
            }
        }).attachToRecyclerView(recyclerView);

        FloatingActionButton addbtn = findViewById(R.id.button_add);
        addbtn.setOnClickListener(v -> startActivity(new Intent(ListProducts.this, addItem.class)));


        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.main_navig);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.nav_orders:
                        Intent intent = new Intent(ListProducts.this, listStoreOrders.class);
                        intent.putExtra("account", "store");
                        intent.putExtra("auth", current);
                        startActivity(intent);
                        break;

                    case R.id.nav_products:
//                        Intent intent2 = new Intent(ListProducts.this, ListProducts.class);
//                        intent2.putExtra("account", "Store");
//                        intent2.putExtra("auth", current);
//                        startActivity(intent2);
                        break;


                    case R.id.nav_history:
                        Intent intent3 = new Intent(ListProducts.this, Archived.class);
                        intent3.putExtra("account", "Store");
                        intent3.putExtra("auth", current);
                        startActivity(intent3);
                        break;



                }
                return false;
            }
        });

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