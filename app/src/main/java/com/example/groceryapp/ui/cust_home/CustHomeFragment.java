package com.example.groceryapp.ui.cust_home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groceryapp.Display_empty_store_page;
import com.example.groceryapp.Product;
import com.example.groceryapp.R;
import com.example.groceryapp.StoreAdapter;
import com.example.groceryapp.StoreOwner;
import com.example.groceryapp.databinding.FragmentCartBinding;
import com.example.groceryapp.databinding.FragmentCustHomeBinding;
import com.example.groceryapp.ui.cart.CartViewModel;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CustHomeFragment extends Fragment {

    private CustHomeViewModel custHomeViewModel;
    private FragmentCustHomeBinding binding;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    RecyclerView viewer;
    TextView err;
    ProgressBar progress;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        custHomeViewModel =
                new ViewModelProvider(this).get(CustHomeViewModel.class);


        binding = FragmentCustHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        viewer = binding.viewStores;
        err = binding.textCustHome;
        progress = binding.storesProgress;



        final TextView textView = binding.textCustHome;
        custHomeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
                progress.setVisibility(View.VISIBLE);
                fetchStores();
            }
        });
        return root;
    }

    public void showStores(FirestoreRecyclerOptions<StoreOwner> stores) {
//        viewer.setLayoutManager(new LinearLayoutManager(getContext()));
        StoreAdapter adapter = new StoreAdapter(stores);
        viewer.setAdapter(adapter);

        viewer.setHasFixedSize(true);


//        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
//            @Override
//            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
//                return false;
//            }
//
//            @Override
//            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
////                adapter.deleteItem(viewHolder.getBindingAdapterPosition());
//            }
//        }).attachToRecyclerView(viewer);
    }

    public void fetchStores() {

        Query query = db.collection("Store Owners").orderBy("Store Name", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<StoreOwner> stores = new FirestoreRecyclerOptions.Builder<StoreOwner>()
                .setQuery(query, StoreOwner.class)
                .build();

        Toast.makeText(getContext(),  "got query", Toast.LENGTH_SHORT).show();
        showStores(stores);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}