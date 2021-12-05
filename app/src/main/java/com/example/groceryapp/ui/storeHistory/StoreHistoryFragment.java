package com.example.groceryapp.ui.storeHistory;

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

import com.example.groceryapp.databinding.FragmentStoreHistoryBinding;

public class StoreHistoryFragment extends Fragment {

    private StoreHistoryViewModel storeHistoryViewModel;
private FragmentStoreHistoryBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        storeHistoryViewModel =
                new ViewModelProvider(this).get(StoreHistoryViewModel.class);

    binding = FragmentStoreHistoryBinding.inflate(inflater, container, false);
    View root = binding.getRoot();

        final TextView textView = binding.textStoreHistory;
        storeHistoryViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

@Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}