package com.example.groceryapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.Query;


public class ListProducts extends CommonSetUp {

    AdapterProduct adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_products);

        setTB();

        Query query = db.collection("Items").orderBy("Name", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Product> options = new FirestoreRecyclerOptions.Builder<Product>()
                .setQuery(query, Product.class)
                .build();

        setRecyclerViewer(options);

        FloatingActionButton buttonAddProduct = findViewById(R.id.button_add);
        buttonAddProduct.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), addItem.class)));
    }

    @Override
    protected int getLayout(){
        return R.id.productsRecyclerView;
    }

    @Override
    public AdapterProduct getAdapter(FirestoreRecyclerOptions<Product> options) {
        return new AdapterProduct(options);
    }

    @Override
    public RecyclerView setRecyclerViewer(FirestoreRecyclerOptions<Product> options){
        RecyclerView rV = super.setRecyclerViewer(options);
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                adapter.deleteItem(viewHolder.getBindingAdapterPosition());
            }
        }).attachToRecyclerView(rV);

        return rV;
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