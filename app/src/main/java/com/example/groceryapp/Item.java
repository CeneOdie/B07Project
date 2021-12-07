package com.example.groceryapp;

import com.google.firebase.firestore.DocumentReference;

public class Item {
    String Brand, Name, Description, Quantity_Size;
    Double Price;
    DocumentReference Store;

    public Item() {
    }

    public Item(String Brand, String Name, String Description, String Quantity_Size, Double Price, DocumentReference Store) {
        this.Brand = Brand;
        this.Name = Name;
        this.Description = Description;
        this.Quantity_Size = Quantity_Size;
        this.Price = Price;
        this.Store = Store;
    }

    public String getBrand() {
        return Brand;
    }

    public void setBrand(String Brand) {
        this.Brand = Brand;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }

    public String getQuantity_Size() {
        return Quantity_Size;
    }

    public void setQuantity_Size(String Quantity_Size) {
        this.Quantity_Size = Quantity_Size;
    }

    public Double getPrice() {
        return Price;
    }

    public void setPrice(Double Price) {
        this.Price = Price;
    }

    public DocumentReference getStore() {
        return Store;
    }

    public void setStore(DocumentReference Store) {
        this.Store = Store;
    }
}
