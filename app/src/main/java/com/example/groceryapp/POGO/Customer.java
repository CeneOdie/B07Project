package com.example.groceryapp.POGO;

import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;

public class Customer {
    String UID, Name, Email;
    ArrayList<DocumentReference> Orders;

    public Customer() {
    }

    public Customer(String UID, String Name, String Email, ArrayList<DocumentReference> Orders) {
        this.UID = UID;
        this.Name = Name;
        this.Email = Email;
        this.Orders = Orders;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
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
}
