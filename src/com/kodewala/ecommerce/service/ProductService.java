package com.kodewala.ecommerce.service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import com.kodewala.ecommerce.exception.ProductNotFoundException;
import com.kodewala.ecommerce.model.Product;
import com.kodewala.ecommerce.repository.ProductRepository;

public class ProductService {
    private ProductRepository productRepository;
    public ProductService(ProductRepository productRepository) { this.productRepository = productRepository; }

    public void addProduct(Product product) {
        if (productRepository.findById(product.getProductId()) != null) {
            System.out.println("A product with this ID already exists.");
            return;
        }
        productRepository.addProduct(product);
        System.out.println("Product added successfully.");
    }
    public List<Product> getAllProducts() { return productRepository.getAllProducts(); }
    public Product findById(String id) throws ProductNotFoundException {
        Product product = productRepository.findById(id);
        if (product == null) throw new ProductNotFoundException("Product with ID " + id + " does not exist.");
        return product;
    }
    public List<Product> searchByName(String name) {
        return productRepository.getAllProducts().stream()
                .filter(p -> p.getProductName().toLowerCase().contains(name.toLowerCase())).collect(Collectors.toList());
    }
    public List<Product> searchByCategory(String category) {
        return productRepository.getAllProducts().stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category)).collect(Collectors.toList());
    }
    public List<Product> searchByBrand(String brand) {
        return productRepository.getAllProducts().stream()
                .filter(p -> p.getBrand().equalsIgnoreCase(brand)).collect(Collectors.toList());
    }
    public List<Product> searchByPrice(double min, double max) {
        return productRepository.getAllProducts().stream().filter(p -> p.getPrice() >= min && p.getPrice() <= max)
                .sorted(Comparator.comparingDouble(Product::getPrice)).collect(Collectors.toList());
    }
    public void updatePrice(String id, double price) throws ProductNotFoundException {
        if (price < 0) throw new IllegalArgumentException("Price cannot be negative.");
        findById(id).setPrice(price);
        System.out.println("Price updated.");
    }
    public void updateQuantity(String id, int quantity) throws ProductNotFoundException {
        if (quantity < 0) throw new IllegalArgumentException("Quantity cannot be negative.");
        findById(id).setQuantity(quantity);
        System.out.println("Quantity updated.");
    }
    public void deleteProduct(String id) throws ProductNotFoundException {
        Product product = findById(id);
        productRepository.deleteProduct(product);
        System.out.println("Product deleted.");
    }
}
