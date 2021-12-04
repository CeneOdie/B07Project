package com.example.groceryapp;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;

public abstract class CommonSetUp extends AppCompatActivity {

    protected FirebaseFirestore db = FirebaseFirestore.getInstance();

    protected void setTB(){
        // Hide default toolbar and its title & display custom toolbar
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        setSupportActionBar(findViewById(R.id.archivedTB));

        // Return to ListProducts activity
        FloatingActionButton btnBack = findViewById(R.id.backbtn);
        btnBack.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), ListProducts.class)));
    }

    protected RecyclerView setRecyclerViewer(FirestoreRecyclerOptions<Product> options) {
        RecyclerView recyclerView = findViewById(getLayout());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(getAdapter(options));
        return recyclerView;
    }

    abstract int getLayout();

    @SuppressWarnings("rawtypes")
    abstract RecyclerView.Adapter getAdapter(FirestoreRecyclerOptions<Product> options);
}
