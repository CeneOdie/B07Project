package com.example.groceryapp;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class AdapterProduct extends FirestoreRecyclerAdapter<Product, AdapterProduct.ProductViewHolder> {

    public AdapterProduct(@NonNull FirestoreRecyclerOptions<Product> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Product model) {
        holder.name.setText(model.getName());
        holder.brand.setText(model.getBrand());
        holder.description.setText(model.getDescription());
        holder.price.setText(String.valueOf(model.getPrice()));
        holder.qs.setText(model.getQs());
        holder.edit.setOnClickListener(v -> {
            String docID = getSnapshots().getSnapshot(position).getId();
            Intent intent = new Intent(v.getContext(), editItem.class);
            intent.putExtra("docID", docID);
            intent.putExtra("product", model);
            v.getContext().startActivity(intent);
        });
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_product, parent, false);
        return new ProductViewHolder(view);
    }

    // Store owner can delete their products
    public void deleteItem(int position) {
        getSnapshots().getSnapshot(position).getReference().delete()
                .addOnSuccessListener(unused -> {
                    // Notify customers who had this item on their cart
                })
                .addOnFailureListener(e -> {
                    // Toast
                });
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView name, brand, description, price, qs;
        Button edit;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.productCard_productName);
            brand = itemView.findViewById(R.id.productCard_productBrand);
            description = itemView.findViewById(R.id.productCard_productDesc);
            price = itemView.findViewById(R.id.productCard_productPrice);
            qs = itemView.findViewById(R.id.productCard_productQS);
            edit = itemView.findViewById(R.id.buttonEdit);
        }
    }
}