package com.example.groceryapp;

public class Item {
    String brand;
    String description;
    String name;
    double price;
    String size;
    StoreOwner store;

    public Item() {

    }

    public Item(String _brand, String _description, String _name, double _price, String _size, StoreOwner _store) {
        brand = _brand;
        description = _description;
        name = _name;
        price = _price;
        size = _size;
        store = _store;
    }



}
