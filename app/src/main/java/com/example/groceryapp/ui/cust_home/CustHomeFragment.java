package com.example.groceryapp.ui.cust_home;

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

import com.example.groceryapp.StoreAdapter;
import com.example.groceryapp.StoreOwner;
import com.example.groceryapp.databinding.FragmentCustHomeBinding;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

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

    public void showStores(ArrayList<StoreOwner> stores) {
        StoreAdapter adapter = new StoreAdapter(getActivity(), stores);
        viewer.setAdapter(adapter);

        viewer.setHasFixedSize(true);

        progress.setVisibility(View.GONE);
    }

    public void fetchStores() {

        ArrayList<StoreOwner> stores = new ArrayList<>();


        db.collection("Store Owners").orderBy("Store Name", Query.Direction.ASCENDING)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        err.setText(error.getMessage());
                        return;
                    }
                    if(value.isEmpty()) {
                        err.setText("No stores to display.Check back later.");
                    } else {
                        for(DocumentSnapshot doc : value.getDocuments()) {
                            StoreOwner newstore = new StoreOwner(doc.getData());
                            if (!stores.contains(newstore)) stores.add(newstore);


                        }
                        err.setText("Showing " + stores.size() + " stores");
                        showStores(stores);
                    }

                });


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}