package com.example.groceryapp;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Source;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class StoreOwner {
    String UID, StoreName, Address, Name, Email;
    ArrayList<Order> Orders;
    ArrayList<Product> Products;
    ArrayList<DocumentReference> OrderRefs;

    public void setOrders(ArrayList<Order> orders) {
        Orders = orders;
    }

    public ArrayList<Product> getProducts() {
        return Products;
    }

    public void setProducts(ArrayList<Product> products) {
        Products = products;
    }

    public ArrayList<DocumentReference> getOrderRefs() {
        return OrderRefs;
    }

    public void setOrderRefs(ArrayList<DocumentReference> orderRefs) {
        OrderRefs = orderRefs;
    }

    public ArrayList<DocumentReference> getItemRefs() {
        return ItemRefs;
    }

    public void setItemRefs(ArrayList<DocumentReference> itemRefs) {
        ItemRefs = itemRefs;
    }

    ArrayList<DocumentReference> ItemRefs;
    FirebaseFirestore db;


    public String getUID() {
        return UID;
    }

    public String getStoreName() {
        return StoreName;
    }

    public String getAddress() {
        return Address;
    }

    public String getName() {
        return Name;
    }

    public String getEmail() {
        return Email;
    }

    public ArrayList<DocumentReference> getOrders() {
        return OrderRefs;
    }

    public ArrayList<DocumentReference> getItems() {
        return ItemRefs;
    }

    public StoreOwner(Map<String, Object> data) {
        UID = (String) data.get("UID");
        StoreName = (String) data.get("Store Name");
        Address = (String) data.get("Address");
        Name = (String) data.get("Name");
        Email = (String) data.get("Email");
        OrderRefs = (ArrayList<DocumentReference>) data.get("Orders");
        ItemRefs = (ArrayList<DocumentReference>) data.get("Items");
    }

    public StoreOwner(DocumentReference doc) {
        doc.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                UID = doc.getId();
                Name = (String) task.getResult().get("Name");
                StoreName = (String) task.getResult().get("Store Name");
                Email = (String) task.getResult().get("Email");
                OrderRefs = (ArrayList<DocumentReference>) task.getResult().get("Orders");
            }
        });
    }


    public StoreOwner(String UID, String storeName, String address, String name, String email, ArrayList<DocumentReference> orders, ArrayList<DocumentReference> items, FirebaseFirestore db) {
        this.UID = UID;
        this.StoreName = storeName;
        this.Address = address;
        this.Name = name;
        this.Email = email;
        this.OrderRefs = orders;
        this.ItemRefs = items;
        this.db = db;
    }

    public StoreOwner() {}

    public void setUID(String UID) {
        this.UID = UID;
    }
    public void setStoreName(String StoreName) {
        this.StoreName = StoreName;
    }
    public void setName(String Name) {
        this.Name = Name;
    }
    public void setEmail(String Email) {
        this.Email = Email;
    }
    public void setAddress(String Address) {
        this.Address = Address;
    }



}
