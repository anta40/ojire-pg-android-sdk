package com.ojire.app.opgdemo;

public class CartItem {
    private int id;
    private String name;
    private int price;
    private int imageRes;
    private int quantity;

    public CartItem(int id, String name, int price, int imageRes) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageRes = imageRes;
        this.quantity = 1; // Default quantity
    }

    // Getters and Setters
    public String getName() { return name; }
    public int getPrice() { return price; }
    public int getImageRes() { return imageRes; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}