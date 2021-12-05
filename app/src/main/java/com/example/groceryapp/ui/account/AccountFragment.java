package com.example.groceryapp.ui.account;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.groceryapp.CustomerNav;
import com.example.groceryapp.MainActivity;
import com.example.groceryapp.R;
import com.example.groceryapp.SetupStore;
import com.example.groceryapp.StoreNav;
import com.example.groceryapp.databinding.FragmentAcountBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class AccountFragment extends Fragment {

    AccountViewModel accountViewModel;
    private FragmentAcountBinding binding;
    Button deleteAcc;
    Activity host;
    Context context;
    FirebaseUser current;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        accountViewModel =
                new ViewModelProvider(this).get(AccountViewModel.class);

        binding = FragmentAcountBinding.inflate(inflater, container, false);
        View root = binding.getRoot();




        final TextView textAccountName = binding.textAccountName;
        TextView accountEmail = binding.textAccountEmail;
        TextView accountype = binding.textAccountType;
        TextView storename = binding.textAccountOwner;
        TextView storeaddress = binding.textAccountAddress;

        Button logout = binding.btnLogout;
        accountViewModel.getText().observe(getViewLifecycleOwner(), s -> {
            host = getActivity();
            context = getActivity().getApplicationContext();
            Bundle extras = getActivity().getIntent().getExtras();
            current = (FirebaseUser) extras.get("auth");
            String username;
            if (current.getDisplayName() == null) username ="User";
            else username =  current.getDisplayName();

            String account = extras.getString("account");
            textAccountName.setText(username);
            accountEmail.setText(current.getEmail());



            logout.setOnClickListener(view -> {
                //logout
                goToHome();
            });
            deleteAcc = binding.btnDeleteAccount;
            deleteAcc.setOnClickListener(view -> {
                AlertDialog confirm = new AlertDialog.Builder(getActivity())
                        .setTitle("Deleting Account")
                        .setMessage("Are you sure you want to delete your account? All open orders will be deleted as well. This cannot be undone.")
                        .setIcon(R.drawable.ic_delete)
                        .setPositiveButton("Delete", (dialogInterface, i) -> {
                            deleteAccount();
                            goToHome();
                        }).setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss()).create();
                confirm.show();

            });


            Button otherbtn = root.findViewById(R.id.button2);
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            accountype.setText(account);
            switch (account) {
                case "Customer":
                    textAccountName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_customer, 0, 0, 0);
                    storename.setVisibility(View.GONE);
                    storeaddress.setVisibility(View.GONE);

                    db.collection("Store Owners").document(current.getUid()).get().addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult() != null) {
                            DocumentSnapshot doc = task.getResult();
                            otherbtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_store, 0, 0, 0);

                            if (doc.exists()) {

                                otherbtn.setText("Switch to Store Account : " + doc.get("Store Name"));

                                otherbtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        goToStoreView();
                                    }
                                });
                            } else {
                                otherbtn.setText(current.getUid());
                                otherbtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_new_store, 0, 0, 0);
                                otherbtn.setText("Set up store account");
                                otherbtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        gotoStoreSetup();
                                    }
                                });
                            }
                        } else {
                            otherbtn.setClickable(false);
                            otherbtn.setText("Please refresh to access store account.");
                        }
                    });

                    break;
                case "Store":
                    storename.setVisibility(View.VISIBLE);
                    storeaddress.setVisibility(View.VISIBLE);
                    db.collection("Store Owners").document(current.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (!task.isSuccessful() || task.getResult() == null) {
                                storename.setText("Please refresh to access store information");
                                storeaddress.setText("");
                            } else {
                                DocumentSnapshot doc = task.getResult();

                                if (doc.exists()) {

                                    textAccountName.setText(doc.getString("Store Name"));
                                    storename.setText(username);
                                    storeaddress.setText(doc.getString("Address"));
//                        goToStoreView();
                                } else {
                                    storename.setText("Not found");
                                    storeaddress.setText("Not found");
//                        gotoStoreSetup();
                                }
                            }
                        }
                    });
                    textAccountName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_store, 0, 0, 0);
//                    textAccountName.setText();

                    otherbtn.setText("Switch to Customer Account : " + username);
                    otherbtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_customer, 0, 0, 0);

                    otherbtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            goToCustView();
                        }
                    });
                    break;

                default:
                    storename.setVisibility(View.GONE);
                    storeaddress.setVisibility(View.GONE);
                    otherbtn.setText("Go to Customer Account");
                    otherbtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            goToCustView();
                        }
                    });
                    break;
            }

        });





        return root;
    }

    public boolean deleteAccount() {

        deleteStore();
        deleteCustomer();
        boolean userdeleted = deleteUser();

        //notify affected users?

        return userdeleted;

    }

    public boolean deleteUser() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if (current != null) {
            String uid = current.getUid();
            current.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(context, "Deleting user "  + uid, Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(context, "Unable to delete user "+ uid, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            return false;
        }
        return true;
    }

    public void deleteStore() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if (current != null) {


            //delete orders
            String uid = current.getUid();
            DocumentReference storedoc = db.collection("Store Owners").document(uid);

            boolean found = storedoc.get().isSuccessful();
            if (found) {
                //delete orders
                db.collection("Orders")
                        .whereEqualTo("Store", storedoc)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful() && task.getResult() != null) {

                                    QuerySnapshot docs = task.getResult();
                                    if (docs.isEmpty()) {
                                        Toast.makeText(context, "No orders for store", Toast.LENGTH_SHORT).show();
                                    } else {
                                        for (DocumentSnapshot doc : docs) {
                                            db.collection("Orders").document(doc.getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(context, "Deleted store order " + doc.getId(), Toast.LENGTH_SHORT).show();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(context, "Unable to delete store order " + doc.getId(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    }
                                } else {
                                    Toast.makeText(context, "Could not get orders for store", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                //delete items
                db.collection("Items")
                        .whereEqualTo("Store", storedoc)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful() && task.getResult()!=null) {

                                    QuerySnapshot docs = task.getResult();
                                    if (docs.isEmpty()) {
                                        Toast.makeText(context, "No items for store", Toast.LENGTH_SHORT).show();
                                    } else {
                                        for (DocumentSnapshot doc : docs) {
                                            db.collection("Items").document(doc.getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(context, "Deleted store item " + doc.getId(), Toast.LENGTH_SHORT).show();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(context, "Unable to delete store item " + doc.getId(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    }
                                } else {
                                    Toast.makeText(context, "Could not get items for store", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                //delete store
                db.collection("Store Owners")
                        .document(uid)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(context, "Deleting store account " + uid, Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Unable to eleting store account " + uid, Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(context, "No store for " + uid, Toast.LENGTH_SHORT);
            }

        }

    }

    public void deleteCustomer() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if (current != null) {

            String uid = current.getUid();
            DocumentReference custdoc = db.collection("Customers").document(uid);

            boolean found = custdoc.get().isSuccessful();

            if (!found) {
                Toast.makeText(context, "No Customer account found for " + uid, Toast.LENGTH_SHORT);
            } else {
                //delete orders
                db.collection("Orders")
                        .whereEqualTo("Customer", custdoc)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful() && task.getResult() != null) {

                                    QuerySnapshot docs = task.getResult();
                                    if (docs.isEmpty()) {
                                        Toast.makeText(context, "No orders for customer", Toast.LENGTH_SHORT).show();
                                    } else {
                                        for (DocumentSnapshot doc : docs) {
                                            db.collection("Orders").document(doc.getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(context, "Deleted customer order " + doc.getId(), Toast.LENGTH_SHORT).show();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(context, "Unable to delete customer order " + doc.getId(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    }
                                } else {
                                    Toast.makeText(context, "Could not get orders for customer", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                //delete customer
                db.collection("Customers")
                        .document(uid)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(context, "Deleting Customer account " + uid, Toast.LENGTH_SHORT).show();
//                            deleteAcc.setText("Deleting customer account " + uid);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Unable to delete Customer account " + uid, Toast.LENGTH_SHORT).show();
                    }
                });
            }

        }

    }





    public void goToCustView() {
        Intent intent = new Intent(getActivity(), CustomerNav.class);
        intent.putExtra("account", "Customer");
        intent.putExtra("auth", current);
        startActivity(intent);
    }


    public void goToStoreView() {
        Intent intent = new Intent(getActivity(), StoreNav.class);
        intent.putExtra("account", "Store");
        intent.putExtra("auth", current);
        startActivity(intent);
    }

    public void gotoStoreSetup() {
        Intent intent = new Intent(getActivity(), SetupStore.class);
        intent.putExtra("account", "Customer");
        intent.putExtra("auth", current);
        startActivity(intent);
    }

    public void goToHome() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}