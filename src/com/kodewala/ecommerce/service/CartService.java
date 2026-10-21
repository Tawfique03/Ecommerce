package com.kodewala.ecommerce.service;

import java.util.List;
import com.kodewala.ecommerce.exception.InvalidQuantityException;
import com.kodewala.ecommerce.exception.ProductNotFoundException;
import com.kodewala.ecommerce.exception.InsufficientStockException;
import com.kodewala.ecommerce.model.CartItem;
import com.kodewala.ecommerce.model.Product;
import com.kodewala.ecommerce.repository.CartRepository;

public class CartService {
    private CartRepository cartRepository;
    private ProductService productService;
    public CartService(CartRepository cartRepository, ProductService productService) {
        this.cartRepository = cartRepository; this.productService = productService;
    }
    public void addProduct(int customerId, String productId, int quantity)
            throws ProductNotFoundException, InvalidQuantityException, InsufficientStockException {
        if (quantity <= 0) throw new InvalidQuantityException("Quantity must be greater than zero.");
        Product product = productService.findById(productId);
        List<CartItem> cart = cartRepository.getCart(customerId);
        CartItem existing = null;
        for (CartItem item : cart) if (item.getProductId().equalsIgnoreCase(productId)) existing = item;
        int alreadyInCart = existing == null ? 0 : existing.getQuantity();
        if (alreadyInCart + quantity > product.getQuantity()) throw new InsufficientStockException("Not enough stock available.");
        if (existing == null) cart.add(new CartItem(product.getProductId(), product.getProductName(), product.getPrice(), quantity));
        else existing.setQuantity(alreadyInCart + quantity);
        System.out.println("Product added to cart.");
    }
    public List<CartItem> getCart(int customerId) { return cartRepository.getCart(customerId); }
    public double getTotal(int customerId) {
        return getCart(customerId).stream().mapToDouble(CartItem::getTotalPrice).sum();
    }
    public void removeProduct(int customerId, String productId) {
        boolean removed = cartRepository.getCart(customerId).removeIf(i -> i.getProductId().equalsIgnoreCase(productId));
        System.out.println(removed ? "Product removed from cart." : "Product was not found in your cart.");
    }
    public void clearCart(int customerId) { cartRepository.clearCart(customerId); }
}
