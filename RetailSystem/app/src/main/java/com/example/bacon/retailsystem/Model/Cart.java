package com.example.bacon.retailsystem.Model;

public class Cart {

    private int quantity;
    private double price;
    private String id, name, date, time, imageUrl;

    public Cart() {
        this.quantity = 0;
        this.price = 0.0;
        this.id = "";
        this.name = "";
        this.date = "";
        this.time = "";
        this.imageUrl = "";
    }

    public Cart(int quantity, double price, String id, String name, String date, String time, String imageUrl) {
        this.quantity = quantity;
        this.price = price;
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.imageUrl = imageUrl;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
