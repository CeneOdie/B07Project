package com.example.groceryapp;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.Map;

public class Product implements Parcelable {

    String name, brand, description, store, qs, itemid;
    double price;
    DocumentReference storeRef;

    public Product() {
    }

    public Product(String name, String brand, String description, String store, String qs, double price) {
        this.name = name;
        this.brand = brand;
        this.description = description;
        this.store = store;
        this.qs = qs;
        this.price = price;
    }

    public Product(Map<String, Object> data, String id) {
        itemid = id;
        name = (String) data.get("Name");
        brand = (String) data.get("Brand");
        description = (String) data.get("Description");
        qs = (String) data.get("Quantity");
        storeRef = (DocumentReference) data.get("Store");
    }

    protected Product(Parcel in) {
        name = in.readString();
        brand = in.readString();
        description = in.readString();
        store = in.readString();
        qs = in.readString();
        price = in.readDouble();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public String getQs() {
        return qs;
    }

    public void setQs(String qs) {
        this.qs = qs;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(brand);
        dest.writeString(description);
        dest.writeString(store);
        dest.writeString(qs);
        dest.writeDouble(price);
    }
}
