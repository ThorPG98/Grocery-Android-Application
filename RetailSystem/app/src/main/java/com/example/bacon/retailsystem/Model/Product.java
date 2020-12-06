package com.example.bacon.retailsystem.Model;

public class Product {

    private int quantity;
    private double price;
    private String id, name, description, imageUrl, category;

    public Product () {
        id = "";
        name = "";
        quantity = 0;
        description = "";
        price = 0.0;
        imageUrl = "";
        category = "";

    }

    public Product(String id, String name, int quantity, String imageUrl, String description, double price, String category) {
        this.quantity = quantity;
        this.price = price;
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
