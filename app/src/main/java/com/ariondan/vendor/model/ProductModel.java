package com.ariondan.vendor.model;

/**
 * Created by AoD Akitektuo on 30-Jul-17 at 02:52.
 */

public class ProductModel {
    private int id;
    private String name;
    private String image;
    private double price;
    private String description;
    private String category;

    public ProductModel(int id, String name, String image, double price, String description, String category) {
        setId(id);
        setName(name);
        setImage(image);
        setPrice(price);
        setDescription(description);
        setCategory(category);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
