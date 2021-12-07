package com.example.groceryapp;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

public class References {

    public static ArrayList<DocumentSnapshot> getReferences (ArrayList<DocumentReference> refs) {
        ArrayList<DocumentSnapshot> snaps = new ArrayList<DocumentSnapshot>();
        for (DocumentReference ref: refs) {
            ref.get().addOnSuccessListener(documentSnapshot -> snaps.add(documentSnapshot));
        }
        return snaps;
    }
}
