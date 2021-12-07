package com.example.groceryapp;

// THIS IS AN ADAPTER FOR DISPLAYING CONCURRENT ORDERS OF THE STORE
// BY CONCURRENT, IT MEANS THAT THE ORDER HAS NOT BEEN ARCHIVED YET

import android.view.View;

import androidx.annotation.NonNull;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdapterOrderMain extends AdapterOrder {

    public AdapterOrderMain(@NonNull FirestoreRecyclerOptions<Order> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull OrderViewHolder holder, int position, @NonNull Order model) {
        super.onBindViewHolder(holder, position, model);

        holder.archive.setVisibility(View.VISIBLE);
        holder.archive.setOnClickListener(v -> {
            FirebaseFirestore.getInstance().collection("Orders")
                    .document(getSnapshots().getSnapshot(position).getId()).update("Archived", true);
        });
    }
}
