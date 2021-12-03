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

public class StoreOwner extends User{
    String StoreID;
    String StoreName;
    String StoreAddr;
    ArrayList<Order> orders;
    ArrayList<Item> items;
    FirebaseFirestore db;



    public void createNew(User user, String _StoreName, String _StoreAddress) {
        Map<String, Object> data = new HashMap<>();
        data.put("Name", user.name);
        data.put("Email", user.email);
        data.put("Store Name", _StoreName);
        data.put("Address", _StoreAddress);
        data.put("UID", user.UID);
        data.put("Orders", Arrays.asList());
        data.put("Items", Arrays.asList());
        db.collection("Customers").add(data);
    }

    public void copyStoreOwner(StoreOwner store) {
        StoreID = store.StoreID;
        UID = store.UID;
        name = store.name;
        email = store.email;
        orders = store.orders;
        items = store.items;
        StoreName = store.StoreName;
        StoreAddr = store.StoreAddr;
    }



    @Override
    public void save() {
        Map<String, Object> data = new HashMap<>();
        data.put("Name", name);
        data.put("Email", email);
        data.put("UID", UID);
        data.put("Store Name", StoreName);
        data.put("Address", StoreAddr);

        //save items
        //save orders

        db.collection("Customers").document(StoreID).set(data);

    }

    public void getStore(String _StoreID) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        StoreOwner store = new StoreOwner();
        DocumentReference doc = db.collection("Customers").document(_StoreID);
        final boolean[] setNull = {false};
        doc.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    setNull[0] = true;
                }
                if (value != null && value.exists()) {
                    store.StoreID = _StoreID;
                    store.name = value.getString("Name");
                    store.email = value.getString("Email");
                    store.UID = value.getString("UID");

                    //get items
                    //get orders

//                    store.items = value.get("Items");
//                    store.orders = value.get("Orders");
                } else {
                    setNull[0] = true;
                }
                copyStoreOwner(store);
            }
        });

    }

    public StoreOwner() {

    }

    public StoreOwner(String _name, String _email, String _UID, String _StoreName, String _StoreAddr) {
        User user = new User(_name, _email, _UID);
        String id = user.getStore();
        if (!TextUtils.isEmpty(id)) {
            createNew(user, _StoreName, _StoreAddr);
        }
        getStore(id);
    }

    public StoreOwner(String _StoreID) {
        getStore(_StoreID);

    }

    public static StoreOwner create(DocumentReference doc) {
        FirebaseFirestore dbs = FirebaseFirestore.getInstance();
        final StoreOwner[] newOwner = {new StoreOwner()};
        dbs.collection("Store Owners").document(doc.getId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Map<String, Object> data = documentSnapshot.getData();
                newOwner[0] = new StoreOwner(String.valueOf(data.get("Name")), String.valueOf(data.get("Email")), String.valueOf(data.get("UID")), String.valueOf(data.get("Store Name")), String.valueOf(data.get("Address")));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                newOwner[0] = null;
            }
        });
        return newOwner[0];
    }


}
