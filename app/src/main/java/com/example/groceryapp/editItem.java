package com.example.groceryapp;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.Map;

public class editItem extends addItem {

    Product product = getIntent().getParcelableExtra("Product");

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_add_item);

        setTB();
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

        // Review getting DocumentReference **********************************
    @Override
    public void saveToFirestore(Map<String, Object> product) {
        db.collection("Items")
                .document(getIntent().getParcelableExtra("documentId"))
                .update(product)
                .addOnSuccessListener(unused -> Toast.makeText(editItem.this, "Updated successfully", Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Toast.makeText(editItem.this, "Error while updating", Toast.LENGTH_SHORT).show());
    }


}
