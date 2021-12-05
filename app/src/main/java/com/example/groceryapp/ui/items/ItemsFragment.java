package com.example.groceryapp.ui.items;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.groceryapp.Product;
import com.example.groceryapp.R;
import com.example.groceryapp.addItem;
import com.example.groceryapp.databinding.FragmentItemsBinding;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ItemsFragment extends Fragment {

    private ItemsViewModel itemsViewModel;
    private FragmentItemsBinding binding;
    AdapterProduct adapter;


    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        // fragment init code
        itemsViewModel =
                new ViewModelProvider(this).get(ItemsViewModel.class);

        binding = FragmentItemsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        // from list product : set up query
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query query = db.collection("Items").orderBy("Name", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<Product> options = new FirestoreRecyclerOptions.Builder<Product>()
                .setQuery(query, Product.class)
                .build();


        // set recycler view (from commonsetup)
        RecyclerView recyclerView = binding.productsViewer;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AdapterProduct(options);
        recyclerView.setAdapter(adapter);

        // set recycler view (from listproducts)
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                adapter.deleteItem(viewHolder.getBindingAdapterPosition());
            }
        }).attachToRecyclerView(recyclerView);


        FloatingActionButton buttonAddProduct = binding.buttonAdd;




        //        buttonAddProduct.setOnClickListener(v -> startActivity(new Intent(getContext(), addItem.class)));
//
//        final TextView textView = binding.textItems;
//        itemsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        return root;
    }


    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }


@Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}