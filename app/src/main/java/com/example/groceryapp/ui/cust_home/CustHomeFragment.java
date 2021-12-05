package com.example.groceryapp.ui.cust_home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.groceryapp.Display_empty_store_page;
import com.example.groceryapp.R;
import com.example.groceryapp.databinding.FragmentCustHomeBinding;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CustHomeFragment extends Fragment {

    private CustHomeViewModel custHomeViewModel;
    private FragmentCustHomeBinding binding;

    public static final String storeKey = "Store Name";
    public static final String ownerKey = "Name";
    public static final String addressKey = "Address";
    public static final String TAG = "stores";
    private List<HashMap<String,String>> stores = new ArrayList<>();
    private SimpleAdapter sa;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        custHomeViewModel =
                new ViewModelProvider(this).get(CustHomeViewModel.class);


        binding = FragmentCustHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        sa = new SimpleAdapter(getActivity(), stores,
                R.layout.view_store_format,
                new String[] { "line 1","line 2","line 3" },
                new int[] {R.id.line_a, R.id.line_b, R.id.line_c});
        listeningChanges();

        final TextView textView = binding.textCustHome;
        custHomeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    public void listeningChanges() {
        db.collection("Stores Owners")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }
                        stores.clear();
                        for (QueryDocumentSnapshot doc : value) {
                            if (doc.get(storeKey) != null && doc.get(ownerKey) != null && doc.get(addressKey) != null) {
                                if (doc.get(storeKey) != "" && doc.get(ownerKey) != "" && doc.get(addressKey) != "") {

                                    HashMap<String, String> temp;
                                    temp = new HashMap<>();
                                    temp.put("line 1", doc.getString(storeKey));
                                    temp.put("line 2", doc.getString(ownerKey));
                                    temp.put("line 3", doc.getString(addressKey));
                                    doc.getData();

                                    stores.add(temp);
                                    ((ListView) binding.storeListView).setAdapter(sa);
                                }
                            }

                            if (stores.isEmpty() == true) {
                                empty_page();
                            }
                        }

                    }
                });
    }

    public void empty_page(){
        Intent intent = new Intent(getActivity(), Display_empty_store_page.class);
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}