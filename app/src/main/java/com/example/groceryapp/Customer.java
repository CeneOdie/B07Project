package com.example.groceryapp;

import android.util.Log;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class Customer {
    String CustID;
    String Name;
    String Email;
    String UID;
    ArrayList<Order> Orders;


    ArrayList<DocumentReference> OrderRefs;

    FirebaseFirestore db = FirebaseFirestore.getInstance();


    public Customer(String custID, String name, String email, String UID, ArrayList<DocumentReference> orderRefs) {
        CustID = custID;
        Name = name;
        Email = email;
        this.UID = UID;
        OrderRefs = orderRefs;
    }

    public Customer(DocumentReference doc) {
        doc.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                CustID = doc.getId();
                UID = (String) task.getResult().get("UID");
                Name = (String) task.getResult().get("Name");
                Email = (String) task.getResult().get("Email");
                OrderRefs = (ArrayList<DocumentReference>) task.getResult().get("Orders");
            }
        });
    }

    public String getCustID() {
        return CustID;
    }

    public void setCustID(String custID) {
        CustID = custID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public ArrayList<Order> getOrders() {
        if (!OrderRefs.isEmpty()) {
            for (DocumentReference ref : OrderRefs) {
                ref.get().addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Order neworder = new Order(documentSnapshot.getData(), documentSnapshot.getId());
                        if (!Orders.contains(neworder)) Orders.add(neworder);
                    } else {
                        Log.d("CUSTOMER_ORDER", "Order " + ref.getId() + " DNE");
                    }
                }).addOnFailureListener(e -> {
                    Log.d("CUSTOMER_ORDER", "Fail create order " + ref.getId());
                });
            }
        }
        return Orders;
    }

    public void setOrders(ArrayList<Order> orders) {
        Orders = orders;
    }

    public ArrayList<DocumentReference> getOrderRefs() {
        return OrderRefs;
    }

    public void setOrderRefs(ArrayList<DocumentReference> orderRefs) {
        OrderRefs = orderRefs;
    }
}
