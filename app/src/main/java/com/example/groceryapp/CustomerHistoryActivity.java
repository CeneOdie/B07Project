package com.example.groceryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseUser;

public class CustomerHistoryActivity extends AppCompatActivity {

    FirebaseUser current;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        Bundle extras = getIntent().getExtras();
        current = (FirebaseUser) extras.get("auth");

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.main_navig);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.nav_stores:
                        Intent intent = new Intent(CustomerHistoryActivity.this, StoreList.class);
                        intent.putExtra("account", "Customer");
                        intent.putExtra("auth", current);
                        startActivity(intent);
                        break;

                    case R.id.nav_cart:
                        Intent intent2 = new Intent(CustomerHistoryActivity.this, CartActivity.class);
                        intent2.putExtra("account", "Customer");
                        intent2.putExtra("auth", current);
                        startActivity(intent2);
                        break;


                    case R.id.nav_cust_history:
//                        Intent intent3 = new Intent(CustomerHistoryActivity.this, CustomerHistoryActivity.class);
//                        intent3.putExtra("account", "Customer");
//                        intent3.putExtra("auth", current);
//                        startActivity(intent3);
                        break;



                }
                return false;
            }
        });
    }
}