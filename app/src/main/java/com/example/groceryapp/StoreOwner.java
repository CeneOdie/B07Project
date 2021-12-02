package com.example.groceryapp;

public class StoreOwner extends User{
    String StoreID;
    String StoreName;
    String StoreAddr;



    public StoreOwner(String _name, String _email, String _UID, String _StoreID, String _StoreName, String _StoreAddr) {
        super(_name, _email, "Store", _UID);
        StoreID = _StoreID;
        StoreName = _StoreName;
        StoreAddr = _StoreAddr;
    }
}
