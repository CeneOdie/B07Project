package com.example.groceryapp;

import com.google.type.DateTime;

import java.util.HashMap;
import java.util.List;

public class Order {
    int count;
    Customer customer;
    DateTime placed;
    HashMap<Item, Integer> items;
    boolean completed;
    StoreOwner store;
    double total;

    public Order() {

    }



    public Order(Customer _customer, DateTime _placed, HashMap<Item, Integer> _items, boolean _completed, StoreOwner _store) {
        customer = _customer;
        placed = _placed;
        items = (HashMap<Item, Integer>) _items.clone();
        completed = _completed;
        store = _store;
        count = items.size();
        total = calculatePrice();

    }

    public double calculatePrice() {
        double result = 0;
        for (Item key : items.keySet()) {
            result += key.price * items.get(key);
        }
        return result;
    }




}
