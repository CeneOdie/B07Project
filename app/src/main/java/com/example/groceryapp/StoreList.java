package com.example.groceryapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StoreList extends AppCompatActivity {
    public static final String storeKey = "Store Name";
    public static final String ownerKey = "Name";
    public static final String addressKey = "Address";
    public static final String TAG = "stores";
    private List<HashMap<String,String>> stores = new ArrayList<>();
    private SimpleAdapter sa;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_stores);
        //Use an Adapter to link data to Views
        sa = new SimpleAdapter(this, stores,
                R.layout.view_store_format,
                new String[] { "line 1","line 2","line 3" },
                new int[] {R.id.line_a, R.id.line_b, R.id.line_c});        listeningChanges();
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

                        if (stores.size() != 0)
                            stores.clear();


                        for (QueryDocumentSnapshot doc : value) {
                            if (doc.get(storeKey) != null || doc.get(ownerKey) != null || doc.get(addressKey) != null) {

                                HashMap<String, String> temp;
                                temp = new HashMap<>();
                                temp.put("line 1", doc.getString(storeKey));
                                temp.put("line 2", doc.getString(ownerKey));
                                temp.put("line 3", doc.getString(addressKey));
                                doc.getData();

                                stores.add(temp);
                                ((ListView) findViewById(R.id.storeListView)).setAdapter(sa);

                            }
                        }

                    }
                });
    }
    //TODO Perry add your code for showing the list of stores here pls

}