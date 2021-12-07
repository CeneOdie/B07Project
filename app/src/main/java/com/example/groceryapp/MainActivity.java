package com.example.groceryapp;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //Please dont change main activity. this routes to signup/login and from there will enter app as a customer of store owner


    RecyclerView viewer;
    TextView err;
    ProgressBar progress;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        err = findViewById(R.id.mainText);
        progress = findViewById(R.id.mainProgress);
        viewer = findViewById(R.id.mainStores);
        fetchStores();

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.main_navig);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){

                    case R.id.nav_login:
                        Intent intent1 = new Intent(MainActivity.this, ShowLoginActivity.class);
                        startActivity(intent1);
                        break;

                    case R.id.nav_main_home:

                        break;



                    case R.id.nav_signup:
                        Intent intent2 = new Intent(MainActivity.this, SignUpActivity.class);
                        startActivity(intent2);
                        break;

                }

                return false;
            }
        });
    }

    public void showStores(ArrayList<StoreOwner> stores) {
        HomeStoreAdapter adapter = new HomeStoreAdapter(this, stores);
        viewer.setAdapter(adapter);

        viewer.setHasFixedSize(true);
        err.setText("Here are our biggest stores");
        progress.setVisibility(View.GONE);
    }

    public void fetchStores() {
        err.setText("Getting our biggest stores... just a moment...");
        progress.setVisibility(View.VISIBLE);

        ArrayList<StoreOwner> stores = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        stores.clear();

        db.collection("Store Owners").orderBy("NumProducts", Query.Direction.DESCENDING).limit(5)
                .get().addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.isEmpty()) err.setText("Unable to get biggest stores at this time. Try again later.");
                    else {
                        for (DocumentSnapshot doc:queryDocumentSnapshots.getDocuments()) {
                            StoreOwner store = doc.toObject(StoreOwner.class);
                            if (store != null) {
                                if (!stores.contains(store)) stores.add(store);
                            }
                        }

                        if (stores.size() == 0)  err.setText("Unable to get biggest stores at this time. Try again later.");
                        else showStores(stores);
                        }

                }).addOnFailureListener(e ->
                        err.setText("Unable to get biggest stores at this time. Try again later."));



    }


    public void showLogin() {
        Intent intent = new Intent(this, ShowLoginActivity.class);
        startActivity(intent);
    }

    public void showSignup(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }


}