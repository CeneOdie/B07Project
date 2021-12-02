package com.example.groceryapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class ListProducts extends AppCompatActivity {

    //TODO Julia list products that a store has to offer

    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private CollectionReference productsRef = database.collection("Products");
    private AdapterProduct adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_products);

        setupRecyclerView();
    }

    private void setupRecyclerView() {
        Query query = productsRef.orderBy("Name", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Product> options = new FirestoreRecyclerOptions.Builder<Product>()
                .setQuery(query, Product.class)
                .build();

        adapter = new AdapterProduct(options);

        RecyclerView recyclerView = findViewById(R.id.productsRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
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