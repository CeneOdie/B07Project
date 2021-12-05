package com.example.groceryapp;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.type.DateTime;

import java.util.HashMap;
import java.util.List;

public class Order implements Parcelable {

    int count;
    Customer customer;
    DateTime placed;
    HashMap<Item, Integer> items;
    boolean archived, completed;
    String status;
    StoreOwner store;
    double subtotal, total;

    public Order() { }

    public Order(int count, Customer customer, DateTime placed, HashMap<Item, Integer> items, boolean archived, boolean completed, String status, StoreOwner store, double subtotal, double total) {
        this.count = items.size();
        this.customer = customer;
        this.placed = placed;
        this.items = (HashMap<Item, Integer>) items.clone();
        this.archived = archived;
        this.completed = completed;
        this.status = status;
        this.store = store;
        this.subtotal = calculateSubtotal();
        this.total = calculateSubtotal() * 1.13;
    }

    protected Order(Parcel in) {
        count = in.readInt();
        archived = in.readByte() != 0;
        completed = in.readByte() != 0;
        status = in.readString();
        subtotal = in.readDouble();
        total = in.readDouble();
    }

    public static final Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel in) {
            return new Order(in);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };

    public double calculateSubtotal(){
        double result = 0;
        for (Item key: items.keySet()) {
            result += key.price * items.get(key);
        }
        return result;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public DateTime getPlaced() {
        return placed;
    }

    public void setPlaced(DateTime placed) {
        this.placed = placed;
    }

    public HashMap<Item, Integer> getItems() {
        return items;
    }

    public void setItems(HashMap<Item, Integer> items) {
        this.items = items;
    }

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public StoreOwner getStore() {
        return store;
    }

    public void setStore(StoreOwner store) {
        this.store = store;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(count);
        dest.writeByte((byte) (archived ? 1 : 0));
        dest.writeByte((byte) (completed ? 1 : 0));
        dest.writeString(status);
        dest.writeDouble(subtotal);
        dest.writeDouble(total);
    }
}
