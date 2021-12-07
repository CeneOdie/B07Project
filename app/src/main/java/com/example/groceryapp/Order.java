package com.example.groceryapp;

import com.google.firebase.firestore.DocumentReference;
import com.google.type.DateTime;

import java.util.ArrayList;
import java.util.Map;

public class Order {
    DocumentReference Store, Customer;
    ArrayList<Map<DocumentReference, Long>> Items;
    boolean Archived, Completed;
    String Status;
    Long Count;
    Double Subtotal, Total;
    DateTime DateTime;

    public Order() {
    }

    public Order(DocumentReference Store,
                 DocumentReference Customer,
                 ArrayList<Map<DocumentReference, Long>> Items,
                 boolean Archived,
                 boolean Completed,
                 String Status,
                 Long Count,
                 Double Subtotal,
                 Double Total,
                 DateTime DateTime) {
        this.Store = Store;
        this.Customer = Customer;
        this.Items = Items;
        this.Archived = Archived;
        this.Completed = Completed;
        this.Status = Status;
        this.Count = Count;
        this.Subtotal = Subtotal;
        this.Total = Total;
        this.DateTime = DateTime;
    }

    public DocumentReference getStore() {
        return Store;
    }

    public void setStore(DocumentReference Store) {
        this.Store = Store;
    }

    public DocumentReference getCustomer() {
        return Customer;
    }

    public void setCustomer(DocumentReference Customer) {
        this.Customer = Customer;
    }

    public ArrayList<Map<DocumentReference, Long>> getItems() {
        return Items;
    }

    public void setItems(ArrayList<Map<DocumentReference, Long>> Items) {
        this.Items = Items;
    }

    public boolean isArchived() {
        return Archived;
    }

    public void setArchived(boolean Archived) {
        this.Archived = Archived;
    }

    public boolean isCompleted() {
        return Completed;
    }

    public void setCompleted(boolean Completed) {
        this.Completed = Completed;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public Long getCount() {
        return Count;
    }

    public void setCount(Long Count) {
        this.Count = Count;
    }

    public Double getSubtotal() {
        return Subtotal;
    }

    public void setSubtotal(Double Subtotal) {
        this.Subtotal = Subtotal;
    }

    public Double getTotal() {
        return Total;
    }

    public void setTotal(Double Total) {
        this.Total = Total;
    }

    public com.google.type.DateTime getDateTime() {
        return DateTime;
    }

    public void setDateTime(com.google.type.DateTime DateTime) {
        this.DateTime = DateTime;
    }
}
