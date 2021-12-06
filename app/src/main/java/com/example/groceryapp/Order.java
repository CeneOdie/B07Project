package com.example.groceryapp;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.type.DateTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Order implements Parcelable {

    long count;
    Customer customer;
    DateTime placed;
    HashMap<Item, Integer> items;
    boolean archived, completed;
    String status, orderid;
    StoreOwner store;
    double subtotal, total;
    DocumentReference storeRef, customerRef;
    HashMap<DocumentReference, Integer> itemRefs;
    Timestamp fromDateTime;

    ArrayList<Object> itemsfromdb;

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

    public static String getTimeDate(long timestamp){
        try{
            DateFormat dateFormat = DateFormat.getDateTimeInstance();
            Date netDate = (new Date(timestamp));
            return dateFormat.format(netDate);
        } catch(Exception e) {
            return "date";
        }
    }

    public Order(Map<String, Object> data, String id) {
        orderid = id;
        count = ((Long) data.get("Count")).intValue();
        customerRef = (DocumentReference) data.get("Customer");
        customer = new Customer(customerRef);
        storeRef = (DocumentReference) data.get("Store");
        fromDateTime = (Timestamp) data.get("DateTime");
        archived = (boolean) data.get("Archived");
        fromDateTime = (Timestamp) data.get("DateTime");

//        DateTime placed;

        completed = (boolean) data.get("Completed");

//        itemRefs -
        itemsfromdb = (ArrayList<Object>) data.get("Items");
//        itemRefs = (HashMap<DocumentReference, Integer>) data.get("Items");
        status = (String) data.get("Status");

        subtotal=((Long) data.get("Subtotal")).doubleValue();
        total = ((Long) data.get("Total")).doubleValue();
    }

    protected Order(Parcel in) {
        count = in.readLong();
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

    public long getCount() {
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
        dest.writeLong(count);
        dest.writeByte((byte) (archived ? 1 : 0));
        dest.writeByte((byte) (completed ? 1 : 0));
        dest.writeString(status);
        dest.writeDouble(subtotal);
        dest.writeDouble(total);
    }
}
