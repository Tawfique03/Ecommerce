package com.kodewala.ecommerce.repository;

import java.util.ArrayList;
import java.util.List;
import com.kodewala.ecommerce.model.Product;

public class ProductRepository {
    private List<Product> products = new ArrayList<>();

    public void addProduct(Product product) { products.add(product); }
    public List<Product> getAllProducts() { return products; }
    public Product findById(String id) {
        for (Product product : products) {
            if (product.getProductId().equalsIgnoreCase(id)) return product;
        }
        return null;
    }
    public void deleteProduct(Product product) { products.remove(product); }
}
