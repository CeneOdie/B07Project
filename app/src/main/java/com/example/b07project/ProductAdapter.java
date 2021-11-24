package com.example.b07project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProductAdapter extends FirebaseRecyclerAdapter<Product, ProductAdapter.Viewholder> {

    public ProductAdapter(@NonNull FirebaseRecyclerOptions<Product> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull Viewholder holder, int position, @NonNull Product model) {
        holder.productName.setText(model.getName());
        holder.productDescription.setText(model.getDescription());
        holder.productPrice.setText(model.getPrice());
        holder.productImg.setImageResource(model.getImage());

    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card, parent, false);
        return new Viewholder(view);
    }

    public static class Viewholder extends RecyclerView.ViewHolder {
        private final ImageView productImg;
        private final TextView productName;
        private final TextView productDescription;
        private final TextView productPrice;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            productImg = itemView.findViewById(R.id.ProductImage);
            productName = itemView.findViewById(R.id.ProductName);
            productDescription = itemView.findViewById(R.id.ProductDescription);
            productPrice = itemView.findViewById(R.id.ProductPrice);
        }
    }
}
