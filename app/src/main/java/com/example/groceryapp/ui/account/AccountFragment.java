package com.example.groceryapp.ui.account;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import Navigation.CustomerNav;
import com.example.groceryapp.MainActivity;
import com.example.groceryapp.R;
import com.example.groceryapp.Auth.SetupStore;
import Navigation.StoreNav;
import com.example.groceryapp.databinding.FragmentAcountBinding;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class AccountFragment extends Fragment {

    AccountViewModel accountViewModel;
    private FragmentAcountBinding binding;
    View root;
    Button deleteAcc, logout, otherbtn;
    Activity host;
    Context context;
    String username, account, uid;
    FirebaseUser current;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView textAccountName, accountEmail, accounttype, storename, storeaddress;
    final static int GO_TO_STORE = 0;
    final static int GO_TO_NEW_STORE = 1;
    final static int GO_TO_CUST = 2;

    public void initViewsAndVars(@NonNull LayoutInflater inflater,
                                 ViewGroup container) {
        accountViewModel =
                new ViewModelProvider(this).get(AccountViewModel.class);

        //from view
        binding = FragmentAcountBinding.inflate(inflater, container, false);
        root = binding.getRoot();

        textAccountName = binding.textAccountName;
        accountEmail = binding.textAccountEmail;
        accounttype = binding.textAccountType;
        storename = binding.textAccountOwner;
        storeaddress = binding.textAccountAddress;

        logout = binding.btnLogout;
        deleteAcc = binding.btnDeleteAccount;
        otherbtn = root.findViewById(R.id.button2);

        // from parent
        host = getActivity();
        context = getActivity().getApplicationContext();
        Bundle extras = getActivity().getIntent().getExtras();
        current = (FirebaseUser) extras.get("auth");
        account = extras.getString("account");
        uid = current.getUid();
        if (current.getDisplayName() == null) username ="User";
        else username =  current.getDisplayName();

    }

    public void toggleStoreInfo(boolean show) {
        if (show) {
            storename.setVisibility(View.VISIBLE);
            storeaddress.setVisibility(View.VISIBLE);
        } else {
            storename.setVisibility(View.GONE);
            storeaddress.setVisibility(View.GONE);
        }
    }

    public void setTextInfo(String defaultCase) { //for default case
        accountEmail.setText(current.getEmail());
        accounttype.setText(account);
        textAccountName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_customer, 0, 0, 0);
        textAccountName.setText(defaultCase);
        toggleStoreInfo(false);
    }

    public void setTextInfo() { //for customer case
        accountEmail.setText(current.getEmail());
        accounttype.setText(account);
        textAccountName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_customer, 0, 0, 0);
        textAccountName.setText(username);
        toggleStoreInfo(false);

    }

    public void setTextInfo(String storeNameText, String storeAddressText) { // for store case
        accountEmail.setText(current.getEmail());
        accounttype.setText(account);
        textAccountName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_store, 0, 0, 0);
        textAccountName.setText(storeNameText);
        toggleStoreInfo(true);
        storename.setText(username);
        storeaddress.setText(storeAddressText);
    }

    public void setButtonInfo() {
        otherbtn.setVisibility(View.GONE);
        deleteAcc.setVisibility(View.GONE);
        setListeners(GO_TO_CUST);
    }

    public void setButtonInfo(String newStore, int icon_id, int listener_switch) {
        otherbtn.setText(newStore);
        otherbtn.setCompoundDrawablesWithIntrinsicBounds(icon_id, 0,0,0);
        setListeners(listener_switch);
    }

    public void setListeners(int listener_switch) {

        logout.setOnClickListener(view -> goToHome());

        deleteAcc.setOnClickListener(view -> {
            AlertDialog confirm = new AlertDialog.Builder(getActivity())
                    .setTitle("Deleting Account")
                    .setMessage("Are you sure you want to delete your account? All open orders will be deleted as well. This cannot be undone.")
                    .setIcon(R.drawable.ic_delete)
                    .setPositiveButton("Delete", (dialogInterface, i) -> {
                        deleteAccount();

                    }).setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss()).create();
            confirm.show();
        });

        otherbtn.setOnClickListener(view -> {
            switch (listener_switch) {
                case GO_TO_STORE:
                    goToStoreView();
                    break;
                case GO_TO_NEW_STORE:
                    gotoStoreSetup();
                    break;
                case GO_TO_CUST:
                    goToCustView();
                    break;
                default:
                    goToCustView();
                    break;
            }
        });


    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        initViewsAndVars(inflater, container);
        switch (account) {
            case "Customer":
                setTextInfo();
                db.collection("Store Owners").document(current.getUid()).get().addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        DocumentSnapshot doc = task.getResult();
                        if (doc.exists()) {
                            setButtonInfo("Switch to Store Account : " + doc.get("Store Name"), R.drawable.ic_store, GO_TO_STORE);
                        } else {
                            setButtonInfo("Set up store account", R.drawable.ic_new_store, GO_TO_NEW_STORE);
                        }
                    } else {
                        setButtonInfo();
                    }
                });
                break;

            case "Store":
                db.collection("Store Owners").document(current.getUid()).get().addOnCompleteListener(task -> {
                    if (!task.isSuccessful() || task.getResult() == null) {
                        setTextInfo("Please refresh to access account information");
                        setButtonInfo();
                    } else {
                        DocumentSnapshot doc = task.getResult();
                        if (doc.exists()) {
                            setTextInfo(doc.getString("Store Name"), doc.getString("Address"));
                        } else {
                            setTextInfo("Please refresh to access account information");
                            setButtonInfo();
                        }
                    }
                });

                setButtonInfo("Switch to Customer Account: " + username, R.drawable.ic_customer, GO_TO_CUST);
                break;

            default:
                setButtonInfo("Go to Customer Account", R.drawable.ic_customer, GO_TO_CUST);
                break;
        }
        return root;
    }

    public void toast(String msg) {
        Toast.makeText(context,  msg, Toast.LENGTH_SHORT).show();
    }

    public void deleteAccount() {
        deleteStore();
        deleteCustomer();
        deleteUser();
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
                        toast("Unable to find store for "))
                .addOnSuccessListener(documentSnapshot -> {
                    //check if store exists
                    if (documentSnapshot.exists()){

                        //find and delete orders
                        deleteCollectionForAccount("Orders", "Store", storedoc);


                        //find and delete items
                        deleteCollectionForAccount("Items", "Store", storedoc);

                        //delete store
                        db.collection("Store Owners")
                                .document(uid)
                                .delete()
                                .addOnSuccessListener(unused ->
                                        toast("Deleting store account " + uid))
                                .addOnFailureListener(e ->
                                        toast("Unable to eleting store account " + uid));
                    } else toast( "No store for " + uid);
            });
        }
    }

    public void deleteCollectionForAccount(String collection, String account, DocumentReference docref) {
        db.collection(collection)
                .whereEqualTo(account, docref)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {

                        QuerySnapshot docs = task.getResult();
                        if (docs.isEmpty()) {
                            toast( "No "+collection+" for "+account);
                        } else {
                            for (DocumentSnapshot doc : docs) {
                                db.collection(collection).document(doc.getId()).delete()
                                        .addOnSuccessListener(unused ->
                                                toast( "Deleted "+collection+" "+account+": " + doc.getId()))
                                        .addOnFailureListener(e ->
                                                toast("Unable to delete "+account+" "+collection+": " + doc.getId()));
                            }
                        }
                    } else {
                        toast("Could not get "+collection+" for "+account);
                    }
                });
    }

    public void deleteCustomer() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if (current != null) {

            String uid = current.getUid();
            DocumentReference custdoc = db.collection("Customers").document(uid);

            custdoc.get().addOnSuccessListener(documentSnapshot -> {

                if (documentSnapshot.exists()){
                    // customer exists
                    deleteCollectionForAccount("Orders", "Customer", custdoc);

                    //delete customer
                    db.collection("Customers")
                            .document(uid)
                            .delete()
                            .addOnSuccessListener(unused -> {
                                toast( "Deleting Customer account " + uid);
                            }).addOnFailureListener(e ->
                                toast( "Unable to delete Customer account " + uid));

                } else {
                    //customer does not exist
                    toast( "No Customer account found for " + uid);
                }

            }).addOnFailureListener(e -> toast( "Could not find customer " + uid));
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