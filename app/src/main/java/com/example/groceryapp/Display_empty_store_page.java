/*
package com.example.sync_store_customer_acc;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class Display_empty_store_page extends AppCompatActivity {
    public static final String TAG = "stores";
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.empty_stores);
        listeningChanges();

    }


    public void listeningChanges() {
        db.collection("stores")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        for (QueryDocumentSnapshot doc : value) {
                            if ((doc.get("name") != null  && doc.get("owner") != null && doc.get("address") != null))
                                if  ((doc.get("name") != "" && doc.get("owner") != "" && doc.get("address") != "")){
                                    back_to_store_page();
                                }
                        }

                    }
                });
    }

    public void back_to_store_page(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}

 */


package com.example.groceryapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class Display_empty_store_page extends AppCompatActivity {
    public static final String TAG = "stores";
    public static final String storeKey = "name";
    public static final String ownerKey = "owner";
    public static final String addressKey = "address";
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.empty_stores);
        listeningChanges();

    }


    public void listeningChanges() {
        db.collection("Stores Owners")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        for (QueryDocumentSnapshot doc : value) {
                            if (doc.get(storeKey) != null && doc.get(ownerKey) != null && doc.get(addressKey) != null) {
                                if (doc.get(storeKey) != "" && doc.get(ownerKey) != "" && doc.get(addressKey) != "") {
                                    back_to_store_page();
                                }
                            }
                        }

                    }
                });
    }

    public void back_to_store_page(){
        Intent intent = new Intent(this, StoreList.class);
        startActivity(intent);
    }
}




















