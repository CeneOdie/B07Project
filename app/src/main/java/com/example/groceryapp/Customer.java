package com.example.groceryapp;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Customer {
    String CustID;
    Order[] Orders;
    String Name;
    String Email;
    String UID;
    ArrayList<DocumentReference> OrderRefs;


    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void save() {
        Map<String, Object> data = new HashMap<>();
        data.put("Name", Name);
        data.put("Email", Email);
        data.put("UID", UID);
        data.put("Orders", Orders);

        db.collection("Customers").document(CustID).set(data);

    }

    public Customer() {

    }


    public Customer(DocumentReference doc) {
        doc.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                UID = doc.getId();
                Name = (String) task.getResult().get("Name");
                Email = (String) task.getResult().get("Email");
                OrderRefs = (ArrayList<DocumentReference>) task.getResult().get("Orders");
            }
        });
    }

    public Customer(String _name, String _email, String _UID) {
        User user = new User(_name, _email, _UID);
        String id = user.getCust();
        if (TextUtils.isEmpty(id)) {
            createNew(user);
        }
        getCustomer(id);
    }

    public void createNew(User user) {
        Map<String, Object> data = new HashMap<>();
        data.put("Name", user.name);
        data.put("Email", user.email);
        data.put("UID", user.UID);
        data.put("Orders", Arrays.asList());
        db.collection("Customers").add(data);
    }

    public void copyCustomer(Customer cust) {
        CustID = cust.CustID;
        UID = cust.UID;
        Name = cust.Name;
        Email = cust.Email;
        Orders = cust.Orders;
    }

    public void getCustomer(String _CustID) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Customer cust = new Customer();
        DocumentReference doc = db.collection("Customers").document(_CustID);
        final boolean[] setNull = {false};
        doc.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    setNull[0] = true;
                }
                if (value != null && value.exists()) {
                    cust.CustID = _CustID;
                    cust.Name = value.getString("Name");
                    cust.Email = value.getString("Email");
                    cust.UID = value.getString("UID");

                    //get orders

                } else {
                    setNull[0] = true;
                }
                copyCustomer(cust);
            }
        });

    }

    public Customer(String _CustID) {
        getCustomer(_CustID);
    }

    public static Customer create(DocumentReference doc) {
        FirebaseFirestore dbs = FirebaseFirestore.getInstance();
        final Customer[] newCustomer = {new Customer()};
        dbs.collection("Customers").document(doc.getId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Map<String, Object> data = documentSnapshot.getData();
                newCustomer[0] = new Customer(String.valueOf(data.get("Name")), String.valueOf(data.get("Email")), String.valueOf(data.get("UID")));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                newCustomer[0] = null;
            }
        });
        return newCustomer[0];
    }
}
