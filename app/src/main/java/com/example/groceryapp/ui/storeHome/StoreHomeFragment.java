package com.example.groceryapp.ui.storeHome;

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
import androidx.recyclerview.widget.RecyclerView;

import com.example.groceryapp.OrderParcel;
import com.example.groceryapp.OrderAdapter;
import com.example.groceryapp.databinding.FragmentStoreHomeBinding;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class StoreHomeFragment extends Fragment {

    private StoreHomeViewModel storeHomeViewModel;
    private FragmentStoreHomeBinding binding;


    FirebaseFirestore db = FirebaseFirestore.getInstance();
    OrderAdapter adapter;

    RecyclerView viewer;
    TextView err;
    ProgressBar progress;
    FirebaseUser current;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        storeHomeViewModel =
                new ViewModelProvider(this).get(StoreHomeViewModel.class);


        binding = FragmentStoreHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        viewer = binding.ordersRecyclerView;
        err = binding.textStoreHome;
        progress = binding.orderProgress;

        Bundle extras = getActivity().getIntent().getExtras();
        current = (FirebaseUser) extras.get("auth");

//        final TextView textView = binding.textCustHome;
        storeHomeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
//                textView.setText(s);
                progress.setVisibility(View.VISIBLE);
//                getOrders();
            }
        });
        return root;
    }

    public void showOrders(ArrayList<OrderParcel> orders) {
        adapter = new OrderAdapter(getActivity(), orders);
        viewer.setAdapter(adapter);

        viewer.setHasFixedSize(true);

        progress.setVisibility(View.GONE);
    }

    public void getOrders() {

        ArrayList<OrderParcel> orders = new ArrayList<>();

        DocumentReference storeref = db.collection("Store Owners").document(current.getUid());

        db.collection("Orders").whereEqualTo("Store", storeref)
//                .orderBy("DateTime", Query.Direction.ASCENDING)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.e("LINK", error.getLocalizedMessage());
                        err.setText(error.getLocalizedMessage());
                        progress.setVisibility(View.GONE);
                        return;
                    }
                    if(value.isEmpty()) {
                        err.setText("No orders to display. Check back later.");
                        progress.setVisibility(View.GONE);

                    } else {
                        for(DocumentSnapshot doc : value.getDocuments()) {
                            OrderParcel neworder = new OrderParcel(doc.getData(), doc.getId());
                            if (!orders.contains(neworder)) orders.add(neworder);


                        }
                        err.setText("Showing " + orders.size() + " orders");
                        showOrders(orders);
                    }
                });
    }

@Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}