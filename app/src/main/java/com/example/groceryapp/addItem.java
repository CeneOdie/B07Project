package com.example.groceryapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class addItem extends AppCompatActivity {

    protected EditText name, brand, description, qs, price;
    protected FirebaseFirestore db;
    protected Button bSave, bCancel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        db = FirebaseFirestore.getInstance();
        setTB();
        assignEditText();

        // Save new item
        saveChanges();

        // Cancel adding new item
        cancelChanges();
    }

    protected void setTB(){
        // Hide default toolbar and its title & display custom toolbar
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        setSupportActionBar(findViewById(R.id.archivedTB));
    }

    protected void assignEditText(){
        name = findViewById(R.id.newProductName);
        brand = findViewById(R.id.newProductBrand);
        description = findViewById(R.id.newProductDescription);
        qs = findViewById(R.id.newProductQuantitySize);
        price = findViewById(R.id.newProductPrice);
    }

    protected void saveChanges(){
        Map<String, Object> newProduct = new HashMap<>();
        bSave = findViewById(R.id.buttonSaveNew);
        bSave.setOnClickListener(v -> {

            // Obtain changes from user input
            String newName = name.getText().toString();
            String newBrand = brand.getText().toString();
            String newDesc = description.getText().toString();
            String newQs = qs.getText().toString();
            String newPrice = price.getText().toString();

            // User needs to at least input name and price
            if (newName.trim().isEmpty() || newPrice.trim().isEmpty()){
                Toast.makeText(addItem.this,
                        "Please enter the name and/or price of your new product",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            // Need to evaluate if newPrice is in correct format
            double finPrice;
            try {
                finPrice = Double.parseDouble(newPrice);
            }catch (NumberFormatException e){
                Toast.makeText(addItem.this, "Please enter only numbers for your price", Toast.LENGTH_SHORT).show();
                return;
            }

            // Evaluate if QS involves numbers?

            // Create a new map to add onto Firestore database
            newProduct.put("Name", newName);
            newProduct.put("Brand", newBrand);
            newProduct.put("Description", newDesc);
            newProduct.put("Quantity_Size", newQs);
            newProduct.put("Price", finPrice);

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String uID = null;
            if (user != null) {
                uID = user.getUid();
            }
            newProduct.put("Store", "/Store Owners/" + uID);

            saveToFirestore(newProduct);
            finish();
//            startActivity(new Intent(getApplicationContext(), ListProducts.class));
        });
    }

    protected void saveToFirestore(Map<String, Object> product){
        db.collection("Items").add(product)
                .addOnSuccessListener(documentReference -> Toast.makeText(addItem.this, "Product successfully added", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(addItem.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    protected void cancelChanges() {
        bCancel = findViewById(R.id.buttonCancelNew);
        bCancel.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), ListProducts.class)));
    }
}