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
    ArrayList<DocumentReference> Orders;
    ArrayList<DocumentReference> Items;
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
        return Orders;
    }

    public ArrayList<DocumentReference> getItems() {
        return Items;
    }

    public StoreOwner(Map<String, Object> data) {
        UID = (String) data.get("UID");
        StoreName = (String) data.get("Store Name");
        Address = (String) data.get("Address");
        Name = (String) data.get("Name");
        Email = (String) data.get("Email");
        Orders = (ArrayList<DocumentReference>) data.get("Orders");
        Items = (ArrayList<DocumentReference>) data.get("Items");
    }

    public StoreOwner(String UID, String storeName, String address, String name, String email, ArrayList<DocumentReference> orders, ArrayList<DocumentReference> items, FirebaseFirestore db) {
        this.UID = UID;
        this.StoreName = storeName;
        this.Address = address;
        this.Name = name;
        this.Email = email;
        this.Orders = orders;
        this.Items = items;
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
    public void setOrders(ArrayList<DocumentReference> Orders) {
        this.Orders = Orders;
    }
    public void setItems(ArrayList<DocumentReference> Items) {
        this.Items = Items;
    }


}
