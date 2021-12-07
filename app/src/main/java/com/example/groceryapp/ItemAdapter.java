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

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    Context context;
    ArrayList<Item> items;


    public ItemAdapter(Context context, ArrayList<Item> items) {
        this.context = context;
        this.items = items;

    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_product, parent, false);
        return new ItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item model = items.get(position);
        holder.name.setText(model.getName());
        holder.brand.setText(model.getBrand());
        holder.description.setText(model.getDescription());
        holder.quantity.setText(model.getQuantity_Size());
        holder.price.setText("$" + String.valueOf(model.getPrice()));
        holder.edit.setClickable(false);

    }



    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView name, brand, description, quantity, price;
        Button edit;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.productCard_productName);
            brand = itemView.findViewById(R.id.productCard_productBrand);
            description = itemView.findViewById(R.id.productCard_productDesc);
            quantity = itemView.findViewById(R.id.productCard_productQS);
            price = itemView.findViewById(R.id.productCard_productPrice);
            edit = itemView.findViewById(R.id.buttonEdit);

        }
    }

}
