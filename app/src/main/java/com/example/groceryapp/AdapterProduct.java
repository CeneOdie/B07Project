package com.example.groceryapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groceryapp.Product;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class AdapterProduct extends FirestoreRecyclerAdapter<Product, AdapterProduct.ProdcutHolder> {

    public AdapterProduct(@NonNull FirestoreRecyclerOptions<Product> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ProdcutHolder holder, int position, @NonNull Product model) {
        holder.name.setText(model.name);
        holder.brand.setText(model.brand);
        holder.description.setText(model.description);
        holder.price.setText(model.price);
    }

    @NonNull
    @Override
    public ProdcutHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_product, parent, false);
        return new ProdcutHolder(view);
    }

    class ProdcutHolder extends RecyclerView.ViewHolder {

        TextView name, brand, description, price;

        public ProdcutHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.productCard_productName);
            brand = itemView.findViewById(R.id.productCard_productBrand);
            description = itemView.findViewById(R.id.productCard_productDesc);
            price = itemView.findViewById(R.id.productCard_productPrice);

        }
    }
}