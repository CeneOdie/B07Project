package com.example.groceryapp.ui.cart;

import androidx.lifecycle.ViewModelProvider;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.groceryapp.R;

public class CartFragment extends Fragment {

    private CartViewModel mViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_cart, container, false);


        View    view = inflater.inflate(R.layout.fragment_cart, container, false);
        view.setBackgroundColor(Color.RED);
        final TextView textView = view.findViewById(R.id.text_cart);
        textView.setText("changed");


        return root;
    }

}