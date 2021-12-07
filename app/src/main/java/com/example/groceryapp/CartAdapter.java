package com.example.groceryapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    Context context;
    Order order;


    public CartAdapter(Context context, Order order) {
        this.context = context;
        this.order = order;

    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
//
        OrderFiller orderfilled = new OrderFiller(order);

//        if (orderfilled.filled()) {
//            holder.orderId.setText(orderfilled.);
//            holder.store.setText(R.id.cartStoreName);
//            holder.storeAddress.setText(R.id.cartStoreAddress);
//            holder.customer.setText(R.id.cartCustomer);
//            holder.itemCount.setText(order.getCount());
//            holder.subtotal.setText(order.getSubtotal());
//            holder.total.setText(order.getTotal());
//        } else {
//            holder.orderId.setText("Unable to load cart");
//            holder.store.setText(R.id.cartStoreName);
//            holder.storeAddress.setText(R.id.cartStoreAddress);
//            holder.customer.setText(R.id.cartCustomer);
//            holder.itemCount.setText(order.getCount());
//            holder.subtotal.setText(order.getSubtotal());
//            holder.total.setText(order.getTotal());
//        }
////        holder.itemList.setText(R.id.allItemList);
//
//        holder.storename.setText(model.getStoreName);
//        holder.address.setText("Address: " + model.getAddress());
//        holder.owner.setText(("Owner: " + model.getName()));
//        holder.openStore.setOnClickListener(view -> {
//            String docID = model.getUID();
//            CartAdapter
////            intent to view items for store
//        });
    }



    @Override
    public int getItemCount() {
        return order.getItems().size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {

        TextView orderId, store, storeAddress, customer, itemCount, subtotal, total;
        ListView itemList;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            // Assign TextViews & ListView
            orderId = itemView.findViewById(R.id.cartOrderID);
            store = itemView.findViewById(R.id.cartStoreName);
            storeAddress = itemView.findViewById(R.id.cartStoreAddress);
            customer = itemView.findViewById(R.id.cartCustomer);
            itemCount = itemView.findViewById(R.id.cartCountNum);
            subtotal = itemView.findViewById(R.id.detailSubtotalPrice);
            total = itemView.findViewById(R.id.detailTotalPrice);
            itemList = itemView.findViewById(R.id.allItemList);

        }
    }

}
