package com.example.groceryapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class listStoreOrders extends AppCompatActivity implements AdapterOrder.OnOrderClickListener {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private AdapterOrderMain adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_store_orders);

        // Hide default toolbar and its title & display custom toolbar
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        setSupportActionBar(findViewById(R.id.archivedTB));

        // Return to ListProducts activity
        FloatingActionButton btnBack = findViewById(R.id.backbtn);
        btnBack.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), ListProducts.class)));

        Query query = db.collection("Orders").orderBy("DateTime", Query.Direction.ASCENDING).whereEqualTo("Archived", true);
        FirestoreRecyclerOptions<OrderParcel> options = new FirestoreRecyclerOptions.Builder<OrderParcel>()
                .setQuery(query, OrderParcel.class)
                .build();
        adapter = new AdapterOrderMain(options);

        RecyclerView recyclerView = findViewById(R.id.ordersRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.setListener(this);

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

    //TODO Julia list of orders for a store (allow for a field of completed/notcompleted when created to use in db query)

}