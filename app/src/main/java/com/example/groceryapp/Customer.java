package com.example.groceryapp;

public class Customer extends User{
    String CustID;

    public Customer(String _name, String _email, String _UID, String _CustId) {
        super(_name, _email, "Customer", _UID);
        CustID = _CustId;
    }
}
