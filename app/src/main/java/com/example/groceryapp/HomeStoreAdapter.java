package com.example.groceryapp;

import static android.view.View.GONE;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HomeStoreAdapter extends RecyclerView.Adapter<HomeStoreAdapter.StoreViewHolder> {

    Context context;
    ArrayList<StoreOwner> stores;


    public HomeStoreAdapter(Context context, ArrayList<StoreOwner> stores) {
        this.context = context;
        this.stores = stores;

    }

    @NonNull
    @Override
    public StoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_store_format, parent, false);
        return new StoreViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull StoreViewHolder holder, int position) {
        StoreOwner model = stores.get(position);
        holder.storename.setText("\t" + model.getStoreName());
        holder.address.setText(model.getAddress());
    }



    @Override
    public int getItemCount() {
        return stores.size();
    }

    public static class StoreViewHolder extends RecyclerView.ViewHolder {

        TextView storename, address;

        public StoreViewHolder(@NonNull View itemView) {
            super(itemView);
            storename = itemView.findViewById(R.id.homeStoreName);
            address = itemView.findViewById(R.id.homeStoreAddress);
        }
    }

}
