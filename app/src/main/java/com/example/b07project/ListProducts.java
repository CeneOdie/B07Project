package com.example.b07project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import android.os.Bundle;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class ListProducts extends AppCompatActivity {

    ProductAdapter adapter;
    DatabaseReference dbref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbref = FirebaseDatabase.getInstance().getReference("Items");
        RecyclerView recyclerView;
        recyclerView = findViewById(R.id.recycler_products);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<Product> options = new FirebaseRecyclerOptions.Builder<Product>().setQuery(dbref, Product.class).build();
        adapter = new ProductAdapter(options);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}