package com.example.groceryapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Map;

public class editItem extends addItem {

    Product product = getIntent().getParcelableExtra("Product");

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_add_item);

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
