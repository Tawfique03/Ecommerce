package com.kodewala.ecommerce.model;

public class Product {
    private String productId;
    private String productName;
    private String category;
    private double price;
    private int quantity;
    private String brand;

    public Product(String productId, String productName, String category, double price, int quantity, String brand) {
        this.productId = productId;
        this.productName = productName;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
        this.brand = brand;
    }

    public String getProductId() { return productId; }
    public String getProductName() { return productName; }
    public String getCategory() { return category; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public String getBrand() { return brand; }
    public void setPrice(double price) { this.price = price; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    @Override
    public String toString() {
        return String.format("ID: %s | Name: %s | Category: %s | Price: ₹%.2f | Stock: %d | Brand: %s",
                productId, productName, category, price, quantity, brand);
    }
}
