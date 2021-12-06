package com.example.groceryapp.ui.items;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groceryapp.AdapterProduct;
import com.example.groceryapp.ItemAdapter;
import com.example.groceryapp.Product;
import com.example.groceryapp.R;
import com.example.groceryapp.StoreAdapter;
import com.example.groceryapp.StoreOwner;
import com.example.groceryapp.addItem;
import com.example.groceryapp.databinding.FragmentCustHomeBinding;
import com.example.groceryapp.databinding.FragmentItemsBinding;
import com.example.groceryapp.ui.cust_home.CustHomeViewModel;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

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

//        final TextView textView = binding.textCustHome;
        itemsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
//                textView.setText(s);
                progress.setVisibility(View.VISIBLE);
                getItems();
            }
        });
        return root;
    }

    public void showItems(ArrayList<Product> items) {
        ItemAdapter adapter = new ItemAdapter(getActivity(), items);
        viewer.setAdapter(adapter);

        viewer.setHasFixedSize(true);

        progress.setVisibility(View.GONE);
    }

    public void getItems() {

        ArrayList<Product> items = new ArrayList<>();

        DocumentReference storeref = db.collection("Store Owners").document(current.getUid());

        db.collection("Items").whereEqualTo("Store", storeref)
//                .orderBy("Name", Query.Direction.ASCENDING)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.e("LINK", error.getLocalizedMessage());
                        err.setText(error.getLocalizedMessage());
                        progress.setVisibility(View.GONE);
                        return;
                    }
                    if(value.isEmpty()) {
                        err.setText("No products to display.Add some by clicking the + button.");
                        progress.setVisibility(View.GONE);

                    } else {
                        for(DocumentSnapshot doc : value.getDocuments()) {
                            Product newitem = new Product(doc.getData(), doc.getId());
                            if (!items.contains(newitem)) items.add(newitem);


                        }
                        err.setText("Showing " + items.size() + " items");
                        showItems(items);

                    }

                });

    }

@Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}