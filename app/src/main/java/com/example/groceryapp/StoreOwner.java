package com.example.groceryapp;


import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;

// POGO for Firestore retrieval
public class StoreOwner {
    String UID, StoreName, Address, Name, Email;//, StoreID;
    ArrayList<DocumentReference> Orders, Items;



    public StoreOwner() {}

    public StoreOwner(String UID,
                      String StoreName,
                      String Address,
                      String Name,
                      String Email,
                      ArrayList<DocumentReference> Orders,
                      ArrayList<DocumentReference> Items) {
        this.UID = UID;
        this.StoreName = StoreName;
        this.Address = Address;
        this.Name = Name;
        this.Email = Email;
        this.Orders = Orders;
        this.Items = Items;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getStoreName() {
        return StoreName;
    }

    public void setStoreName(String StoreName) {
        this.StoreName = StoreName;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }


    public ArrayList<DocumentReference> getOrders() {

        return Orders;
    }

    public void setOrders(ArrayList<DocumentReference> Orders) {
        this.Orders = Orders;
    }

    public ArrayList<DocumentReference> getItems() {

        return Items;
    }

    public void setItems(ArrayList<DocumentReference> Items) {
        this.Items = Items;
    }


}
