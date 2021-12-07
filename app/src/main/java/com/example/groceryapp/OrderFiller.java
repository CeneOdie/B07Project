package com.example.groceryapp;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderFiller {
    Order root;
    Customer customer;
    StoreOwner store;
    Map<Item, Integer> items;
    String orderid;


    public boolean filled() {
        return (root != null && customer != null && store != null && items != null);
    }


    public OrderFiller(Order _root) {
        root = _root;
        if (root != null) {
            getId();
            getCustomer();
            getStore();
            getItems();
        }
    }

    public void getId() {
        FirebaseFirestore.getInstance().collection("Orders");

    }

    public OrderFiller() {
    }

    public void getCustomer() {
        root.getCustomer().get().addOnSuccessListener(documentSnapshot -> {
            Customer load = documentSnapshot.toObject(Customer.class);
            if (load != null) customer = load;
        });
    };

    public void getStore() {
        root.getStore().get().addOnSuccessListener(documentSnapshot -> {
            StoreOwner load = documentSnapshot.toObject(StoreOwner.class);
            if (load != null) store = load;
        });
    };

    public void getItems() {
        Map<Item, Long> loadlist = new HashMap<Item, Long>();

        ArrayList<Map<DocumentReference, Long>> itemmaps = root.getItems();

        for (Map<DocumentReference, Long> map : itemmaps) {
            DocumentReference[] keys = (DocumentReference[]) map.keySet().toArray();
            for (DocumentReference key : keys) {
                key.get().addOnSuccessListener(documentSnapshot -> {
                    Item item = documentSnapshot.toObject(Item.class);
                    if (item != null) {
                        if (!loadlist.keySet().contains(item)) {
                            loadlist.put(item, map.get(key));
                        }


                    }
                });
            }
        }
    }


}
