package com.example.groceryapp.ui.cart;

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

import com.example.groceryapp.OrderParcel;
import com.example.groceryapp.Item;
import com.example.groceryapp.databinding.FragmentCartBinding;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CartFragment extends Fragment {

    private CartViewModel cartViewModel;
    private FragmentCartBinding binding;

    ListenerRegistration listener;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    RecyclerView viewer;
    TextView err;
    ProgressBar progress;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        cartViewModel =
                new ViewModelProvider(this).get(CartViewModel.class);


        binding = FragmentCartBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        viewer = binding.cartViewer;
        err = binding.textCart;
        progress = binding.cartProgress;



//        final TextView textView = binding.textCustHome;
        cartViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
//                textView.setText(s);
                progress.setVisibility(View.VISIBLE);
                fetchCart();
            }
        });
        return root;
    }

    public void showCart(ArrayList<Item> items) {
//        CartAdapter adapter = new CartAdapter(getActivity(), items);
//        viewer.setAdapter(items);

        viewer.setHasFixedSize(true);

        progress.setVisibility(View.GONE);
    }

    public void fetchCart() {

        ArrayList<Item> items = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        items.clear();

        //.orderBy("Store Name", Query.Direction.ASCENDING)
        //                        for(DocumentSnapshot doc : value.getDocuments()) {
        //                            Order order = doc.toObject(Order.class);
        //                            if (order != null)
        //
        //                            StoreOwner newstore = doc.toObject(StoreOwner.class);
        //                            if (newstore != null) {
        //                                if (newstore.getItems().isEmpty()) noproducts++;
        //                                else {
        //                                    if (!stores.contains((newstore))) stores.add(newstore);
        //                                }
        //                            }
        //                        }
        //                        Toast.makeText(getActivity().getApplicationContext(), noproducts + " store(s) have no products", Toast.LENGTH_SHORT).show();
        listener = db.collection("Orders").whereEqualTo("Status", "In Progress").orderBy("DateTime", Query.Direction.ASCENDING)   //.orderBy("Store Name", Query.Direction.ASCENDING)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        err.setText(error.getMessage());
                        return;
                    }
                    if (value.isEmpty()) {
                        err.setText("No cart. Start shopping in the home tab.");
                    } else {
//                        value.getQuery().get().addOnSuccessListener(queryDocumentSnapshots -> {
//                            List<DocumentSnapshot> snaps = queryDocumentSnapshots.getDocuments();
//                            for (DocumentSnapshot doc : snaps) {
//                                OrderParcel order = doc.toObject(OrderParcel.class);
//                                HashMap<DocumentReference, Integer> productsRefs = order.getItems();
//
//                            }
//                        })


//                        for(DocumentSnapshot doc : value.getDocuments()) {
//                            Order order = doc.toObject(Order.class);
//                            if (order != null)
//
//                            StoreOwner newstore = doc.toObject(StoreOwner.class);
//                            if (newstore != null) {
//                                if (newstore.getItems().isEmpty()) noproducts++;
//                                else {
//                                    if (!stores.contains((newstore))) stores.add(newstore);
//                                }
//                            }
//                        }
//                        Toast.makeText(getActivity().getApplicationContext(), noproducts + " store(s) have no products", Toast.LENGTH_SHORT).show();

                        err.setText("Showing " + items.size() + " items in your cart");
                        showCart(items);
                    }
                });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        if (listener != null) listener.remove();
    }
}