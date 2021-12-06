package com.example.groceryapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.ArrayList;

public class StoreAdapter extends FirestoreRecyclerAdapter<StoreOwner, StoreAdapter.StoreViewHolder> {

    Context context;

    public StoreAdapter(@NonNull FirestoreRecyclerOptions<StoreOwner> stores) {
        super(stores);
        Toast.makeText(context,  "created", Toast.LENGTH_SHORT).show();

    }

//
//    public StoreAdapter(Context context, FirestoreRecyclerAdapter<StoreOwner> stores) {
//        this.context = context;
//        this.stores = stores;
//    }

    ArrayList<StoreOwner> stores;

    @NonNull
    @Override
    public StoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        Toast.makeText(context,  "created view holder", Toast.LENGTH_SHORT).show();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_store_format, parent, false);
        return new StoreViewHolder(v);
    }

    @Override
    protected void onBindViewHolder(@NonNull StoreViewHolder holder, int position, @NonNull StoreOwner model) {
        Toast.makeText(context,  "binding view holder", Toast.LENGTH_SHORT).show();
        Toast.makeText(context,  model.getStoreName(), Toast.LENGTH_SHORT).show();
        holder.storename.setText(model.StoreName);
        holder.address.setText(model.Address);
        holder.owner.setText((model.Name));
        holder.openStore.setOnClickListener(view -> {
            String docID = getSnapshots().getSnapshot(position).getId();
            //intent to view items for store
        });
    }

    @Override
    public int getItemCount() {
        return stores.size();
    }

    public static class StoreViewHolder extends RecyclerView.ViewHolder {

        TextView storename, owner, address;
        Button openStore;

        public StoreViewHolder(@NonNull View itemView) {
            super(itemView);
            Log.w("VIEWHOLDER", "Creating view holdeer");
            storename = itemView.findViewById(R.id.line_a);
            owner = itemView.findViewById(R.id.line_b);
            address = itemView.findViewById(R.id.line_c);
            openStore = itemView.findViewById(R.id.see_items);
        }
    }

}
