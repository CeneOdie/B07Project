package com.example.groceryapp;

import android.content.Context;
import android.util.Log;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Store extends StoreOwner{
    private StoreOwner store;
    FirebaseFirestore db;
    Context forToast;

    public void t(String msg) {
        Log.d("STORE", msg);
//        if (forToast != null)
//        Toast.makeText(forToast, msg, Toast.LENGTH_SHORT).show();
    }

    public void setContext(Context context) {
        forToast = context;
    }

    public Store(DocumentSnapshot doc) {
        store = doc.toObject(StoreOwner.class);
        if (store != null) copy(store);
    }

    public Store(StoreOwner store) { if(store != null) copy(store);}

    public Store(DocumentReference doc) {
        doc.get().addOnSuccessListener(documentSnapshot -> {
            store = documentSnapshot.toObject(StoreOwner.class);
            if (store != null) copy(store);
        });
    }

    public Store(String uid) {
        DocumentReference ref = db.collection("Store Owners").document();
        new Store(ref);
    }

    private void copy(StoreOwner store) {
        t("Found store " + getUID());
        this.setUID(store.getUID());
        this.setName(store.getName());
        this.setStoreName(store.getStoreName());
        this.setEmail(store.getEmail());
        this.setAddress(store.getAddress());
        this.setOrders(store.getOrders());
        this.setItems(store.getItems());
    }

}
