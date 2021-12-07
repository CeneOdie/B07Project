package com.example.groceryapp;

// THIS IS A GENERAL ADAPTER FOR HOLDING ORDER CONTENTS
// ADAPTERORDERMAIN EXTENDS THIS ADAPTER

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
import com.google.firebase.firestore.DocumentSnapshot;


public class AdapterOrder extends FirestoreRecyclerAdapter<OrderParcel, AdapterOrder.OrderViewHolder> {

    private OnOrderClickListener listener;

    public AdapterOrder(@NonNull FirestoreRecyclerOptions<OrderParcel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull OrderViewHolder holder, int position, @NonNull OrderParcel model) {
        holder.orderID.setText(getSnapshots().getSnapshot(position).getId());
        holder.customer.setText(model.customer.Name);
        holder.timeStamp.setText(String.valueOf(model.getPlaced()));
        holder.status.setVisibility(View.GONE);
        holder.subtotal.setText(String.valueOf(model.getSubtotal()));
        holder.archive.setVisibility(View.GONE);

        holder.itemView.setOnClickListener(v -> {
            String docID = getSnapshots().getSnapshot(position).getId();
            Intent intent = new Intent(v.getContext(), editItem.class);
            intent.putExtra("docId", docID);
            intent.putExtra("order", model);
            v.getContext().startActivity(intent);
        });
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_order, parent, false);
        return new OrderViewHolder(view);
    }

    public void deleteOrder(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    public void setListener(OnOrderClickListener listener) {
        this.listener = listener;
    }

    public interface OnOrderClickListener {
        void onOrderClick(DocumentSnapshot snapshot, int position);
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView orderID, customer, timeStamp, subtotal, status;
        Button archive;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderID = itemView.findViewById(R.id.cardOrderID);
            customer = itemView.findViewById(R.id.cardOrderCustomer);
            timeStamp = itemView.findViewById(R.id.cardOrderDateTime);
            subtotal = itemView.findViewById(R.id.cardOrderPrice);
            status = itemView.findViewById(R.id.cardOrderStatus);

            itemView.setOnClickListener(v -> {
                int position = getBindingAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null){
                    listener.onOrderClick(getSnapshots().getSnapshot(position), position);
                }
            });
        }
    }
}
