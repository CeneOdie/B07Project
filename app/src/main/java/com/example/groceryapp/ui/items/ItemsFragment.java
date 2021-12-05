package com.example.groceryapp.ui.items;

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

import com.example.groceryapp.databinding.FragmentItemsBinding;

public class ItemsFragment extends Fragment {

    private ItemsViewModel itemsViewModel;
private FragmentItemsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        itemsViewModel =
                new ViewModelProvider(this).get(ItemsViewModel.class);

    binding = FragmentItemsBinding.inflate(inflater, container, false);
    View root = binding.getRoot();

        final TextView textView = binding.textItems;
        itemsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
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