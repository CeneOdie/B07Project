package com.example.groceryapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseUser;

import java.util.Map;

public class editItem extends addItem {

    Product product = getIntent().getParcelableExtra("Product");

    FirebaseUser current;
    String account;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_add_item);


        Bundle extras = getIntent().getExtras();
        current = (FirebaseUser) extras.get("auth");
        account = extras.getString("account");


        setTB();

        // Return to ListProducts activity
        FloatingActionButton btnBack = findViewById(R.id.backbtn);
        btnBack.setOnClickListener(v -> startActivity(new Intent(editItem.this, ListProducts.class)));

        assignEditText();

        // Receive Item document passed with Intent and display on EditTexts
        name.setText(product.getName());
        brand.setText(product.getBrand());
        description.setText(product.getDescription());
        qs.setText(product.getQs());
        price.setText(String.valueOf(product.getPrice()));

        // Save edits
        saveChanges();
        // Cancel edits
        cancelChanges();




        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.main_navig);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.nav_orders:
//                        Intent intent = new Intent(editItem.this, listStoreOrders.class);
//                        intent.putExtra("account", "store");
//                        intent.putExtra("auth", current);
//                        startActivity(intent);
                        break;

                    case R.id.nav_products:
                        Intent intent2 = new Intent(editItem.this, ListProducts.class);
                        intent2.putExtra("account", "Store");
                        intent2.putExtra("auth", current);
                        startActivity(intent2);
                        break;


                    case R.id.nav_history:
//                        Intent intent3 = new Intent(editItem.this, Archived.class);
//                        intent3.putExtra("account", "Store");
//                        intent3.putExtra("auth", current);
//                        startActivity(intent3);
                        break;


                    case R.id.nav_account:
//                        Intent intent4 = new Intent(editItem.this, AccountActivity.class);
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
    public void saveToFirestore(Map<String, Object> product) {
        db.collection("Items")
                .document(getIntent().getParcelableExtra("documentId"))
                .update(product)
                .addOnSuccessListener(unused -> Toast.makeText(editItem.this, "Updated successfully", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(editItem.this, "Error while updating", Toast.LENGTH_SHORT).show());
    }


}
