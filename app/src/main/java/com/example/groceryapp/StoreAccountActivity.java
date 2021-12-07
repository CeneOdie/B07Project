package com.example.groceryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class StoreAccountActivity extends AppCompatActivity {
    Button deleteAcc, logout, otherbtn;
    Context context;
    String username, account, uid;
    FirebaseUser current;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView textAccountName, accountEmail, accounttype, storename, storeaddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_account);

        textAccountName = findViewById(R.id.text_saccount_name);
        accountEmail = findViewById(R.id.text_saccount_email);
        accounttype = findViewById(R.id.text_saccount_type);
        storename = findViewById(R.id.text_saccount_owner);
        storeaddress = findViewById(R.id.text_saccount_address);

        logout = findViewById(R.id.btn_logout);
        deleteAcc = findViewById(R.id.btn_deleteAccount);
        otherbtn = findViewById(R.id.button2);

        Bundle extras = getIntent().getExtras();
        current = (FirebaseUser) extras.get("auth");
        account = extras.getString("account");
        uid = current.getUid();
        if (current.getDisplayName() == null) username ="User";
        else username =  current.getDisplayName();

        logout.setOnClickListener(view -> goToHome());

        deleteAcc.setOnClickListener(view -> {
            AlertDialog confirm = new AlertDialog.Builder(StoreAccountActivity.this)
                    .setTitle("Deleting Account")
                    .setMessage("Are you sure you want to delete your account? All open orders will be deleted as well. This cannot be undone.")
                    .setIcon(R.drawable.ic_delete)
                    .setPositiveButton("Delete", (dialogInterface, i) -> {
                        deleteAccount();

                    }).setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss()).create();
            confirm.show();
        });


                db.collection("Store Owners").document(current.getUid()).get().addOnCompleteListener(task -> {
                    if (!task.isSuccessful() || task.getResult() == null) {
                        accountEmail.setText(current.getEmail());
                        accounttype.setText(account);
                        textAccountName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_customer, 0, 0, 0);
                        textAccountName.setText("Please refresh to access account information");
                        otherbtn.setVisibility(View.GONE);
                        deleteAcc.setVisibility(View.GONE);
                    } else {
                        DocumentSnapshot doc = task.getResult();
                        StoreOwner store = doc.toObject(StoreOwner.class);
                        if (store != null) {
                            accountEmail.setText(current.getEmail());
                            accounttype.setText(account);
                            textAccountName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_store, 0, 0, 0);
                            textAccountName.setText(store.getStoreName());
                            storename.setText(username);
                            storeaddress.setText(store.getAddress());
                        } else {
                            accountEmail.setText(current.getEmail());
                            accounttype.setText(account);
                            textAccountName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_customer, 0, 0, 0);
                            textAccountName.setText("Please refresh to access account information");
                            otherbtn.setVisibility(View.GONE);
                            deleteAcc.setVisibility(View.GONE);
                        }
                    }
                });

                otherbtn.setText("Switch to Customer Account: " + username);
                otherbtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_customer, 0,0,0);
                otherbtn.setOnClickListener(view -> {

                    Intent intent3 = new Intent(StoreAccountActivity.this, StoreList.class);
                    intent3.putExtra("account", "Customer");
                    intent3.putExtra("auth", current);
                    startActivity(intent3);
                });

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.main_navig);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        menu.clear();


        MenuItem menuItem = menu.getItem(3);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.nav_stores:
                        Intent intent = new Intent(StoreAccountActivity.this, listStoreOrders.class);
                        intent.putExtra("account", "Store");
                        intent.putExtra("auth", current);
                        startActivity(intent);
                        break;

                    case R.id.nav_cart:
                        Intent intent2 = new Intent(StoreAccountActivity.this, ListProducts.class);
                        intent2.putExtra("account", "Store");
                        intent2.putExtra("auth", current);
                        startActivity(intent2);
                        break;


                    case R.id.nav_history:
                        Intent intent3 = new Intent(StoreAccountActivity.this, Archived.class);
                        intent3.putExtra("account", "Store");
                        intent3.putExtra("auth", current);
                        startActivity(intent3);
                        break;



                }
                return false;
            }
        });

    }


    public void toast(String msg) {
        Toast.makeText(context,  msg, Toast.LENGTH_SHORT).show();
    }

    public void deleteAccount() {
//        deleteStore();
//        deleteCustomer();
//        deleteUser();
    }

    public void deleteUser() {
        if (current != null) {
            current.delete().addOnSuccessListener(unused -> {
                toast("Deleting user "  + uid);
                goToHome();
            }).addOnFailureListener(e ->
                    toast("Unable to delete user "+ uid));
        } else {
            toast("No user signed in");
        }
    }

    public void deleteStore() {

        if (current != null) {
            DocumentReference storedoc = db.collection("Store Owners").document(uid);
            storedoc.get()
                    .addOnFailureListener(e ->
                            toast("Unable to find store for " + storedoc.getId()))
                    .addOnSuccessListener(documentSnapshot -> {
                        //check if store exists
                        StoreOwner store = documentSnapshot.toObject(StoreOwner.class);
                        if (store == null) toast( "No store for " + uid);
                        else {
                            ArrayList<DocumentReference> orders = store.getOrders();
                            ArrayList<DocumentReference> products = store.getItems();

                            if (orders.isEmpty()) toast("No orders for store.");
                            else deleteRefs(orders, "Deleted order for store: ", "Unable to delete order for store: ");

                            if (products.isEmpty()) toast("No products for store");
                            else deleteRefs(products, "Deleted product for store: ", "Unable to delete product for store: ");

                            storedoc.delete()
                                    .addOnSuccessListener(unused ->
                                            toast("Deleting store account " + uid))
                                    .addOnFailureListener(e ->
                                            toast("Unable to delete store account " + uid));
                        }

                    });
        }
    }

    public void deleteRefs(ArrayList<DocumentReference> docs, String success, String fail) {
        for(DocumentReference doc : docs) {
            doc.delete().addOnSuccessListener(unused -> toast(success + doc.getId())).addOnFailureListener(e -> toast(fail + doc.getId()));
        }
    }



    public void deleteCustomer() {
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        if (current != null) {
//
//            String uid = current.getUid();
//            DocumentReference custdoc = db.collection("Customers").document(uid);
//
//            custdoc.get()
//                    .addOnFailureListener(e -> toast( "Could not find customer " + uid))
//                    .addOnSuccessListener(documentSnapshot -> {
//
//                        Customer cust = documentSnapshot.toObject(Customer.class);
//                        if (cust == null) toast( "No Customer account found for " + uid);
//                        else {
//                            ArrayList<DocumentReference> orders = cust.getOrders();
//                            if (orders.isEmpty()) toast("No orders for customer.");
//                            else deleteRefs(orders, "Deleted order for customer: ", "Unable to delete order for customer: ");
//
//                            custdoc.delete()
//                                    .addOnSuccessListener(unused -> {
//                                        toast( "Deleting Customer account " + uid);
//                                    }).addOnFailureListener(e ->
//                                    toast( "Unable to delete Customer account " + uid));
//
//                        }
//
//
//                    });
//        }
    }


    public void goToCustView() {
        Intent intent = new Intent(this, StoreList.class);
        intent.putExtra("account", "Customer");
        intent.putExtra("auth", current);
        startActivity(intent);
    }



    public void goToHome() {
        Intent intent = new Intent(StoreAccountActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }



}