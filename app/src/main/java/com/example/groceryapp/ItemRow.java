package com.example.groceryapp;

// ItemRow is essentially the same as Product: need to delete and accommated accordingly
// AdapterItemList will be affected

public class ItemRow {

    String id, name;
    double countItem, singlePrice, totPrice;

    public ItemRow() {
    }

    public ItemRow(String id, String name, double countItem, double singlePrice, double totPrice) {
        this.id = id;
        this.name = name;
        this.countItem = countItem;
        this.singlePrice = singlePrice;
        this.totPrice = totPrice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCountItem() {
        return countItem;
    }

    public void setCountItem(double countItem) {
        this.countItem = countItem;
    }

    public double getSinglePrice() {
        return singlePrice;
    }

    public void setSinglePrice(double singlePrice) {
        this.singlePrice = singlePrice;
    }

    public double getTotPrice() {
        return totPrice;
    }

    public void setTotPrice(double totPrice) {
        this.totPrice = totPrice;
    }
}
