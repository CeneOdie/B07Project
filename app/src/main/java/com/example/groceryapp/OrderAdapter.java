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

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    Context context;
    ArrayList<OrderParcel> items;


    public OrderAdapter(Context context, ArrayList<OrderParcel> items) {
        this.context = context;
        this.items = items;

    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_order, parent, false);
        return new OrderViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        OrderParcel model = items.get(position);
        holder.id.setText(model.orderid);
        holder.customer.setText(model.customer.Name);
        holder.datetime.setText(model.fromDateTime.toDate().toString());
        holder.price.setText("$" + String.valueOf(model.total));
        holder.status.setText(model.status);
        holder.archive.setClickable(false);

    }



    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {

        TextView id, customer, datetime, price, status;
        Button archive;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.cardOrderID);
            customer = itemView.findViewById(R.id.cardOrderCustomer);
            datetime = itemView.findViewById(R.id.cardOrderDateTime);
            price = itemView.findViewById(R.id.cardOrderPrice);
            status = itemView.findViewById(R.id.cardOrderStatus);
            archive = itemView.findViewById(R.id.buttonArchive);

        }
    }

}
