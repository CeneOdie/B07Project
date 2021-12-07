package com.example.groceryapp.ui.items;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groceryapp.AdapterProduct;
import com.example.groceryapp.ItemAdapter;
import com.example.groceryapp.Item;
import com.example.groceryapp.StoreOwner;
import com.example.groceryapp.References;
import com.example.groceryapp.databinding.FragmentItemsBinding;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ItemsFragment extends Fragment {

    private ItemsViewModel itemsViewModel;
    private FragmentItemsBinding binding;
    AdapterProduct adapter;

    RecyclerView viewer;
    TextView err;
    ProgressBar progress;
    FirebaseUser current;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        itemsViewModel =
                new ViewModelProvider(this).get(ItemsViewModel.class);


        binding = FragmentItemsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        viewer = binding.productsViewer;
        err = binding.textItems;
        progress = binding.itemsProgress;

        Bundle extras = getActivity().getIntent().getExtras();
        current = (FirebaseUser) extras.get("auth");

        itemsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                progress.setVisibility(View.VISIBLE);
                getItems();
            }
        });
        return root;
    }

    public void showItems(ArrayList<Item> items, String storename) {
        ItemAdapter adapter = new ItemAdapter(getActivity(), items);
        viewer.setAdapter(adapter);

        viewer.setHasFixedSize(true);

        endGet("Showing " + items.size() + " products for " + storename);

    }

    public void endGet(String msg) {
        progress.setVisibility(View.GONE);
        err.setText(msg);
    }

    public void getItems() {

        ArrayList<Item> items = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        items.clear();
        DocumentReference storeref = db.collection("Store Owners").document(current.getUid());
        storeref.addSnapshotListener((value, error) -> {
            if (error != null) {
                endGet(error.getMessage());
                return;
            }
            StoreOwner store = value.toObject(StoreOwner.class);
            if (store == null) {
                items.clear();
                showItems(items, "");
                endGet("Store not found");

            } else {
                ArrayList<DocumentReference> products = store.getItems();
                if (products.isEmpty()) endGet(store.getStoreName() + " has no products. Add some with the + button.");
                else {

                    ArrayList<DocumentSnapshot> productSnaps = References.getReferences(products);

                    for (DocumentSnapshot doc : productSnaps) {
                        Item product = doc.toObject(Item.class);
                        if (product != null && !items.contains(product)) items.add(product);
                    }

                    showItems(items, store.getStoreName());
            }

            }
        });
//                get().addOnSuccessListener(documentSnapshot -> {
//            StoreOwner store = documentSnapshot.toObject(StoreOwner.class);
//            if (store == null) endGet("Store Account not found");
//            else {
//
//                ArrayList<DocumentReference> products = store.getItems();
//                if (products.isEmpty()) endGet(store.getStoreName() + " has no products. Add some with the + button.");
//                else {
//                    for (DocumentReference productref : products) {
//                        Toast.makeText(getActivity().getApplicationContext(), productref.getId(), Toast.LENGTH_SHORT).show();
//                        productref.get().addOnSuccessListener(documentSnapshot1 -> {
//                            Item product = documentSnapshot1.toObject(Item.class);
//
//                            if(product != null) {
//                                Toast.makeText(getActivity().getApplicationContext(), product.getName(), Toast.LENGTH_SHORT).show();
//
//                                if (!items.contains(product))  items.add(product);
//                            }
//                        });
//                    }
//                    showItems(items, store.getStoreName());
//                }
//
//            }
//        }).addOnFailureListener(e -> endGet("Store not found"));

    }

@Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}