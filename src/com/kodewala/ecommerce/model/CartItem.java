package com.kodewala.ecommerce.model;

public class CartItem {
    private String productId;
    private String productName;
    private double price;
    private int quantity;

    public CartItem(String productId, String productName, double price, int quantity) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
    }

    public String getProductId() { return productId; }
    public String getProductName() { return productName; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public double getTotalPrice() { return price * quantity; }

    @Override
    public String toString() {
        return productName + " (ID: " + productId + ") | ₹" + String.format("%.2f", price)
                + " x " + quantity + " = ₹" + String.format("%.2f", getTotalPrice());
    }
}
