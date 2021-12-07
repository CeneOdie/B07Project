package com.example.groceryapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.StoreViewHolder> {

    Context context;
    ArrayList<StoreOwner> stores;


    public StoreAdapter(Context context, ArrayList<StoreOwner> stores) {
        this.context = context;
        this.stores = stores;

    }

    @NonNull
    @Override
    public StoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_store_format, parent, false);
        return new StoreViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull StoreViewHolder holder, int position) {
        StoreOwner model = stores.get(position);
        holder.storename.setText(model.getStoreName());
        holder.address.setText("Address: " + model.getAddress());
        holder.owner.setText(("Owner: " + model.getName()));
        holder.openStore.setOnClickListener(view -> {
//            String docID = model.getUID();StoreAdapter
//            intent to view items for store
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
            storename = itemView.findViewById(R.id.line_a);
            owner = itemView.findViewById(R.id.line_b);
            address = itemView.findViewById(R.id.line_c);
            openStore = itemView.findViewById(R.id.see_items);
        }
    }

}
