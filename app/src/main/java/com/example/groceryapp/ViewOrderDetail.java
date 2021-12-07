package com.example.groceryapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ViewOrderDetail extends AppCompatActivity implements AdapterView.OnItemClickListener {

    FirebaseUser current;
    String account;


    TextView orderId, store, storeAddress, customer, dateTime, itemCount, subtotal, total;
    ListView itemList;
    private final DocumentReference docRef = FirebaseFirestore.getInstance().collection("Orders")
            .document(getIntent().getParcelableExtra("docId").toString());



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Bundle extras = getIntent().getExtras();
        current = (FirebaseUser) extras.get("auth");
        account = extras.getString("account");


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
        String spinnerDisplay = (String) order.status;
        switch (spinnerDisplay) {
            case "Order received":
                spinner.setSelection(0);
                break;
            case "In progress":
                spinner.setSelection(1);
                break;
            case "Completed":
                spinner.setSelection(2);
                break;
            default:
                spinner.setSelection(3);
                break;
        }
        spinner.setOnItemClickListener(this);



        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.main_navig);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();

        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.nav_orders:
//                        Intent intent = new Intent(ViewOrderDetail.this, listStoreOrders.class);
//                        intent.putExtra("account", "store");
//                        intent.putExtra("auth", current);
//                        startActivity(intent);
                        break;

                    case R.id.nav_products:
//                        Intent intent2 = new Intent(ViewOrderDetail.this, ListProducts.class);
//                        intent2.putExtra("account", "Store");
//                        intent2.putExtra("auth", current);
//                        startActivity(intent2);
                        break;


                    case R.id.nav_history:
//                        Intent intent3 = new Intent(ViewOrderDetail.this, Archived.class);
//                        intent3.putExtra("account", "Store");
//                        intent3.putExtra("auth", current);
//                        startActivity(intent3);
                        break;


                    case R.id.nav_store_account:
//                        Intent intent4 = new Intent(ViewOrderDetail.this, StoreAccountActivity.class);
//                        intent4.putExtra("account", "Store");
//                        intent4.putExtra("auth", current);
//                        startActivity(intent4);
                        break;

                }
                return false;
            }
        });


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();

        if (text.equals("Archived")) {
            docRef.update("Archived", true)
                    .addOnSuccessListener(unused -> docRef.update("Status", text)
                            .addOnSuccessListener(v -> Toast.makeText(ViewOrderDetail.this, "Updated successfully", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e -> Toast.makeText(ViewOrderDetail.this, "Error while updating", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e -> Toast.makeText(ViewOrderDetail.this, "Error while updating", Toast.LENGTH_SHORT).show()));
        } else if (text.equals("Completed")) {
            docRef.update("Completed", true)
                    .addOnSuccessListener(unused -> docRef.update("Status", text)
                            .addOnSuccessListener(v -> Toast.makeText(ViewOrderDetail.this, "Updated successfully", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e -> Toast.makeText(ViewOrderDetail.this, "Error while updating", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e -> Toast.makeText(ViewOrderDetail.this, "Error while updating", Toast.LENGTH_SHORT).show()));
        } else {
            docRef.update("Status", text)
                    .addOnSuccessListener(unused -> Toast.makeText(ViewOrderDetail.this, "Updated successfully", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(ViewOrderDetail.this, "Error while updating", Toast.LENGTH_SHORT).show());
        }
    }
}