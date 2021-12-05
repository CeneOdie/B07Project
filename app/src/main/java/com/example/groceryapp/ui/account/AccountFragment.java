package com.example.groceryapp.ui.account;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.groceryapp.MainActivity;
import com.example.groceryapp.R;
import com.example.groceryapp.SetupStore;
import com.example.groceryapp.StoreNav;
import com.example.groceryapp.databinding.FragmentAcountBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AccountFragment extends Fragment {

    private AccountViewModel accountViewModel;
    private FragmentAcountBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        accountViewModel =
                new ViewModelProvider(this).get(AccountViewModel.class);

        binding = FragmentAcountBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textAccount;
        Button logout = binding.btnLogout;
        accountViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
                logout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //logout
                        goToHome();
                    }
                });
            }
        });


        Button deleteAcc = binding.btnDeleteAccount;
        deleteAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog confirm = new AlertDialog.Builder(getActivity())
                        .setTitle("Deleting Account")
                        .setMessage("Are you sure you want to delete your account? All open orders will be deleted as well. This cannot be undone.")
                        .setIcon(R.drawable.ic_delete)
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
//                                delete account
                                goToHome();
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).create();
                confirm.show();

            }
        });

        Bundle extras = getActivity().getIntent().getExtras();
        FirebaseUser current = (FirebaseUser) extras.get("logged in");
        Button otherbtn = root.findViewById(R.id.button2);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Store Owners").document(current.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    if (doc.exists()) {
                        otherbtn.setText("Switch to Store Account");
                        otherbtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                goToStoreView();
                            }
                        });
//                        goToStoreView();
                    } else {
                        otherbtn.setText("Set up store account");
                        otherbtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                gotoStoreSetup();
                            }
                        });
//                        gotoStoreSetup();
                    }
                } else {
                    otherbtn.setClickable(false);
                    otherbtn.setText("Please refresh to access store account.");
                }
            }
        });


        return root;
    }

    public void goToStoreView() {
        Intent intent = new Intent(getActivity(), StoreNav.class);
        intent.putExtras(getActivity().getIntent().getExtras());
//        intent.addFlags(F)
        startActivity(intent);
    }

    public void gotoStoreSetup() {
        Intent intent = new Intent(getActivity(), SetupStore.class);
        intent.putExtras(getActivity().getIntent().getExtras());
        startActivity(intent);
    }

    public void goToHome() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}