package com.example.groceryapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class AdapterItemList extends ArrayAdapter<ItemRow> {

    AdapterItemList(Context context, ArrayList<ItemRow> itemRowArrayList) {
        super(context, R.layout.single_item, itemRowArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ItemRow item = getItem(position);
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.single_item, parent, false);

        TextView id, name, count, singlePrice, totPrice;
        id = convertView.findViewById(R.id.itemID);
        name = convertView.findViewById(R.id.itemName);
        count = convertView.findViewById(R.id.itemCount);
        singlePrice = convertView.findViewById(R.id.itemPrice);
        totPrice = convertView.findViewById(R.id.itemTotPrice);

        id.setText(item.getId());
        name.setText(item.getName());
        count.setText("(" + String.valueOf(item.getCountItem()) + ")     X");
        singlePrice.setText(String.valueOf(item.getSinglePrice()));
        totPrice.setText(String.valueOf(totPrice));

        return super.getView(position, convertView, parent);
    }
}
