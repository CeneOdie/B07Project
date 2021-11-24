package com.example.b07project;

public class Product {

    private String Brand, Description;
    private int Image;
    private String Name, Note, Price, Quantity_Size, Store;

    public Product() {
    }

    public Product(String brand, String description, int image, String name, String note, String price, String quantity_Size, String store) {
        Brand = brand;
        Description = description;
        Image = image;
        Name = name;
        Note = note;
        Price = price;
        Quantity_Size = quantity_Size;
        Store = store;
    }

    public String getBrand() {
        return Brand;
    }

    public void setBrand(String brand) {
        Brand = brand;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getImage() {
        return Image;
    }

    public void setImage(int image) {
        Image = image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getQuantity_Size() {
        return Quantity_Size;
    }

    public void setQuantity_Size(String quantity_Size) {
        Quantity_Size = quantity_Size;
    }

    public String getStore() {
        return Store;
    }

    public void setStore(String store) {
        Store = store;
    }
}
