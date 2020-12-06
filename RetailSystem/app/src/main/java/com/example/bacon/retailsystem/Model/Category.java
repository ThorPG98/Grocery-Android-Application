package com.example.bacon.retailsystem.Model;

public class Category {

    String id, name;
    int imageUrl;

    public Category() {
        id = "";
        imageUrl = 0;
        name = "";
    }

    public Category(String id, String name, int imageUrl) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(int imageUrl) {
        this.imageUrl = imageUrl;
    }
}
