package com.example.groceryapp.ui.cust_history;

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

import com.example.groceryapp.Order;
import com.example.groceryapp.OrderAdapter;
import com.example.groceryapp.databinding.FragmentCustHistoryBinding;
import com.example.groceryapp.databinding.FragmentStoreHistoryBinding;
import com.example.groceryapp.ui.storeHistory.StoreHistoryViewModel;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class CustHistoryFragment extends Fragment {

    private CustHistoryViewModel custHistoryViewModel;
    private FragmentCustHistoryBinding binding;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    OrderAdapter adapter;

    RecyclerView viewer;
    TextView err;
    ProgressBar progress;
    FirebaseUser current;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        custHistoryViewModel =
                new ViewModelProvider(this).get(CustHistoryViewModel.class);


        binding = FragmentCustHistoryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        viewer = binding.ordersCustHistoryRecyclerView;
        err = binding.textCustHistory;
        progress = binding.orderCustHistoryProgress;

        Bundle extras = getActivity().getIntent().getExtras();
        current = (FirebaseUser) extras.get("auth");

//        final TextView textView = binding.textCustHome;
        custHistoryViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
//                textView.setText(s);
                progress.setVisibility(View.VISIBLE);
//                getOrders();
            }
        });
        return root;
    }

    public void showOrders(ArrayList<Order> orders) {
        adapter = new OrderAdapter(getActivity(), orders);
        viewer.setAdapter(adapter);

        viewer.setHasFixedSize(true);

        progress.setVisibility(View.GONE);
    }

    public void getOrders() {

        ArrayList<Order> orders = new ArrayList<>();

        DocumentReference custref = db.collection("Customers").document(current.getUid());

        db.collection("Orders").whereEqualTo("Customer", custref).whereEqualTo("Completed", true)
//                .orderBy("DateTime", Query.Direction.ASCENDING)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.e("LINK", error.getLocalizedMessage());
                        err.setText(error.getLocalizedMessage());
                        progress.setVisibility(View.GONE);
                        return;
                    }
                    if(value.isEmpty()) {
                        err.setText("No completed orders to display. View any open orders on the cart page.");
                        progress.setVisibility(View.GONE);

                    } else {
                        for(DocumentSnapshot doc : value.getDocuments()) {
                            Order neworder = new Order(doc.getData(), doc.getId());
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