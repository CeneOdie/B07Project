package com.example.groceryapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ViewOrderDetail extends AppCompatActivity implements AdapterView.OnItemClickListener {

    TextView orderId, store, storeAddress, customer, dateTime, itemCount, subtotal, total;
    ListView itemList;
    private final DocumentReference docRef = FirebaseFirestore.getInstance().collection("Orders")
            .document(getIntent().getParcelableExtra("docId").toString());



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_order_detail);

        // Assign TextViews & ListView
        orderId = findViewById(R.id.detailOrderID);
        store = findViewById(R.id.detailStoreName);
        storeAddress = findViewById(R.id.detailStoreAddress);
        customer = findViewById(R.id.detailCustomer);
        dateTime = findViewById(R.id.detailDateTime);
        itemCount = findViewById(R.id.detailCountNum);
        subtotal = findViewById(R.id.detailSubtotalPrice);
        total = findViewById(R.id.detailTotalPrice);
        itemList = findViewById(R.id.allItemList);

        Order order = getIntent().getParcelableExtra("order");
        orderId.setText(getIntent().getParcelableExtra("docId").toString());
        store.setText(order.store.getStoreName());
        storeAddress.setText(order.store.getAddress());
        customer.setText(order.customer.Name);
        dateTime.setText(String.valueOf(order.placed));
        itemCount.setText(String.valueOf(order.count));
        subtotal.setText(String.valueOf(order.getSubtotal()));
        total.setText(String.valueOf(order.getTotal()));

        // set ListView
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null){
                DocumentSnapshot doc = task.getResult();
                if (doc.exists()){
                    List<Map<String, Integer>> items = (List<Map<String, Integer>>) doc.get("Items");
                    ArrayList<String> itemName = new ArrayList<>();
                    ArrayList<Integer> itemCount = new ArrayList<>();
                    for (Map<String, Integer> item: items) {
                        String name = String.valueOf(item.get("Name"));
                        Integer count = item.get("Quantity");
                        itemName.add(name);
                        itemCount.add(count);
                    }
                }
            }
        });

        // spinner
        Spinner spinner = findViewById(R.id.spinnerStatus);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.order_status, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();

        if (text.equals("Archived")) {
            docRef.update("Archived", true)
                    .addOnSuccessListener(unused -> Toast.makeText(ViewOrderDetail.this, "Updated successfully", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(ViewOrderDetail.this, "Error while updating", Toast.LENGTH_SHORT).show());
        } else if (text.equals("Completed")) {
            docRef.update("Completed", true)
                    .addOnSuccessListener(unused -> Toast.makeText(ViewOrderDetail.this, "Updated successfully", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(ViewOrderDetail.this, "Error while updating", Toast.LENGTH_SHORT).show());
        }

        // update status in Firestore
        docRef.update("Status", text)
                .addOnSuccessListener(unused ->
                        Toast.makeText(ViewOrderDetail.this, "Updated successfully", Toast.LENGTH_SHORT).show()
                ).addOnFailureListener(e ->
                        Toast.makeText(ViewOrderDetail.this, "Error while updating", Toast.LENGTH_SHORT).show()
        );
    }
}