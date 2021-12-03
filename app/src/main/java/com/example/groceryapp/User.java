package com.example.groceryapp;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public class User {
    String name;
    String email;
    String UID;
    FirebaseFirestore db;
    static FirebaseAuth auths;

    StoreOwner store;
    Customer customer;



    public static User signin(String email, String pass) {
        final User[] user = {new User()};

        auths.signInWithEmailAndPassword(email, pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
           @Override
           public void onSuccess(AuthResult authResult) {
               FirebaseUser current = auths.getCurrentUser();
               user[0].name = current.getDisplayName();
               user[0].UID = current.getUid();
               user[0].email = current.getEmail();

           }
       }).addOnFailureListener(new OnFailureListener() {
           @Override
           public void onFailure(@NonNull Exception e) {
               user[0] = null;
           }
       });
       return user[0];

    }

    public static void signout() {
        auths.signOut();
    }

    public User(){
        auths = FirebaseAuth.getInstance();
    }

    public User(String _name, String _email, String _UID) {
        auths = FirebaseAuth.getInstance();
        name = _name;
        email = _email;
        UID = _UID;
        db = FirebaseFirestore.getInstance();
    }

    public String getCust() {
        final String[] found = {""};
        if (!TextUtils.isEmpty(UID)) {

            db.collection("Customers").whereEqualTo("UID", UID).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult().size() == 1) {
                                    for (QueryDocumentSnapshot doc : task.getResult()) {
                                        found[0] = doc.getId();
                                    }
                                }
                            }
                        }
                    });
        }
        return found[0];
    }

    public String getStore() {
        final String[] found = {""};
        if (!TextUtils.isEmpty(UID)) {
            db.collection("Store Owners").whereEqualTo("UID", UID).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult().size() == 1){
                                    for (QueryDocumentSnapshot doc: task.getResult()) {
                                        found[0] = doc.getId();
                                    }
                                }
                            }
                        }
                    });
        }

        return found[0];
    }

    public void save() {

    }


}
