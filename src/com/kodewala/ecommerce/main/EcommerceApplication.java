package com.kodewala.ecommerce.main;

import java.util.List;
import java.util.Scanner;
import com.kodewala.ecommerce.exception.*;
import com.kodewala.ecommerce.model.*;
import com.kodewala.ecommerce.repository.*;
import com.kodewala.ecommerce.service.*;

public class EcommerceApplication {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        ProductRepository productRepository = new ProductRepository();
        CustomerRepository customerRepository = new CustomerRepository();
        CartRepository cartRepository = new CartRepository();
        OrderRepository orderRepository = new OrderRepository();
        ProductService productService = new ProductService(productRepository);
        CustomerService customerService = new CustomerService(customerRepository);
        CartService cartService = new CartService(cartRepository, productService);
        OrderService orderService = new OrderService(orderRepository, customerService, productService, cartService);
        loadInitialProducts(productRepository);

        while (true) {
            System.out.println("\n================================");
            System.out.println("       E-COMMERCE APPLICATION");
            System.out.println("================================");
            System.out.println("1. Admin");
            System.out.println("2. Customer");
            System.out.println("3. Exit");
            int choice = readInt("Enter your choice: ");
            switch (choice) {
                case 1 -> adminMenu(productService, customerService, orderService);
                case 2 -> customerMenu(productService, customerService, cartService, orderService);
                case 3 -> { System.out.println("Application closed."); return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static void loadInitialProducts(ProductRepository repository) {
        repository.addProduct(new Product("P101", "Dell Laptop", "Electronics", 55000, 10, "Dell"));
        repository.addProduct(new Product("P102", "HP Laptop", "Electronics", 50000, 8, "HP"));
        repository.addProduct(new Product("P103", "Wireless Mouse", "Accessories", 1200, 20, "Logitech"));
        repository.addProduct(new Product("P104", "Samsung Monitor", "Electronics", 18000, 5, "Samsung"));
        repository.addProduct(new Product("P105", "Apple Keyboard", "Accessories", 7500, 7, "Apple"));
    }

    private static void adminMenu(ProductService products, CustomerService customers, OrderService orders) {
        while (true) {
            System.out.println("\n========== ADMIN MENU ==========");
            System.out.println("1. Add Product");
            System.out.println("2. View Products");
            System.out.println("3. Search Product");
            System.out.println("4. Update Product");
            System.out.println("5. Delete Product");
            System.out.println("6. View Customers");
            System.out.println("7. View All Orders");
            System.out.println("8. Exit");
            switch (readInt("Enter your choice: ")) {
                case 1 -> addProduct(products);
                case 2 -> showProducts(products.getAllProducts());
                case 3 -> searchProducts(products);
                case 4 -> updateProduct(products);
                case 5 -> { try { products.deleteProduct(readText("Enter product ID: ")); } catch (Exception e) { showError(e); } }
                case 6 -> { for (Customer c : customers.getAllCustomers()) System.out.println(c); }
                case 7 -> { if (orders.getAllOrders().isEmpty()) System.out.println("No orders yet."); else for (Order o : orders.getAllOrders()) System.out.println(o); }
                case 8 -> { return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static void addProduct(ProductService service) {
        String id = readText("Product ID: ");
        String name = readText("Product name: ");
        String category = readText("Category: ");
        double price = readDouble("Price: ");
        int quantity = readInt("Quantity: ");
        String brand = readText("Brand: ");
        if (price < 0 || quantity < 0) { System.out.println("Price and quantity cannot be negative."); return; }
        service.addProduct(new Product(id, name, category, price, quantity, brand));
    }

    private static void searchProducts(ProductService service) {
        System.out.println("1. Search by ID");
        System.out.println("2. Search by name");
        System.out.println("3. Search by category");
        System.out.println("4. Search by brand");
        System.out.println("5. Search by price range");
        switch (readInt("Choose search type: ")) {
            case 1 -> { try { System.out.println(service.findById(readText("Product ID: "))); } catch (Exception e) { showError(e); } }
            case 2 -> showProducts(service.searchByName(readText("Product name: ")));
            case 3 -> showProducts(service.searchByCategory(readText("Category: ")));
            case 4 -> showProducts(service.searchByBrand(readText("Brand: ")));
            case 5 -> { double min = readDouble("Minimum price: "); double max = readDouble("Maximum price: "); showProducts(service.searchByPrice(min, max)); }
            default -> System.out.println("Invalid choice.");
        }
    }

    private static void updateProduct(ProductService service) {
        String id = readText("Product ID: ");
        System.out.println("1. Update price");
        System.out.println("2. Update quantity");
        try {
            int choice = readInt("Choose option: ");
            if (choice == 1) service.updatePrice(id, readDouble("New price: "));
            else if (choice == 2) service.updateQuantity(id, readInt("New quantity: "));
            else System.out.println("Invalid choice.");
        } catch (Exception e) { showError(e); }
    }

    private static void customerMenu(ProductService products, CustomerService customers, CartService cart, OrderService orders) {
        while (true) {
            System.out.println("\n========= CUSTOMER MENU =========");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. View Products");
            System.out.println("4. Search Product");
            System.out.println("5. Add Product to Cart");
            System.out.println("6. View Cart");
            System.out.println("7. Remove Product from Cart");
            System.out.println("8. Place Order");
            System.out.println("9. View My Orders");
            System.out.println("10. Cancel Order");
            System.out.println("11. Logout");
            switch (readInt("Enter your choice: ")) {
                case 1 -> customers.register(readText("Name: "), readText("Email: "), readText("Mobile: "), readText("Address: "));
                case 2 -> login(customers, cart, orders);
                case 3 -> showProducts(products.getAllProducts());
                case 4 -> searchProducts(products);
                case 5 -> addToCart(customers, cart, products);
                case 6 -> viewCart(customers, cart);
                case 7 -> removeFromCart(customers, cart);
                case 8 -> placeOrder(customers, orders);
                case 9 -> viewOrders(customers, orders);
                case 10 -> cancelOrder(customers, orders);
                case 11 -> { return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static void login(CustomerService customers, CartService cart, OrderService orders) {
        try {
            int id = readInt("Enter your customer ID: ");
            Customer customer = customers.findById(id);
            System.out.println("Welcome, " + customer.getName() + ". You are now the active customer.");
            // Keep the learning project simple: customer actions ask for the ID when needed.
        } catch (Exception e) { showError(e); }
    }

    private static void addToCart(CustomerService customers, CartService cart, ProductService products) {
        try {
            int customerId = readInt("Customer ID: "); customers.findById(customerId);
            String productId = readText("Product ID: "); int quantity = readInt("Quantity: ");
            cart.addProduct(customerId, productId, quantity);
        } catch (Exception e) { showError(e); }
    }
    private static void viewCart(CustomerService customers, CartService cart) {
        try {
            int id = readInt("Customer ID: "); customers.findById(id);
            List<CartItem> items = cart.getCart(id);
            if (items.isEmpty()) System.out.println("Your cart is empty.");
            else { for (CartItem item : items) System.out.println(item); System.out.printf("Cart total: ₹%.2f%n", cart.getTotal(id)); }
        } catch (Exception e) { showError(e); }
    }
    private static void removeFromCart(CustomerService customers, CartService cart) {
        try { int id = readInt("Customer ID: "); customers.findById(id); cart.removeProduct(id, readText("Product ID to remove: ")); }
        catch (Exception e) { showError(e); }
    }
    private static void placeOrder(CustomerService customers, OrderService orders) {
        try { orders.placeOrder(readInt("Customer ID: ")); }
        catch (Exception e) { showError(e); }
    }
    private static void viewOrders(CustomerService customers, OrderService orders) {
        try {
            int id = readInt("Customer ID: "); customers.findById(id);
            List<Order> list = orders.getCustomerOrders(id);
            if (list.isEmpty()) System.out.println("No orders found."); else for (Order o : list) { System.out.println(o); for (CartItem item : o.getItems()) System.out.println("  " + item); }
        } catch (Exception e) { showError(e); }
    }
    private static void cancelOrder(CustomerService customers, OrderService orders) {
        try { int customerId = readInt("Customer ID: "); int orderId = readInt("Order ID: "); orders.cancelOrder(customerId, orderId); }
        catch (Exception e) { showError(e); }
    }
    private static void showProducts(List<Product> products) {
        if (products.isEmpty()) System.out.println("No products found."); else for (Product product : products) System.out.println(product);
    }
    private static String readText(String prompt) { System.out.print(prompt); return scanner.nextLine().trim(); }
    private static int readInt(String prompt) {
        while (true) { try { return Integer.parseInt(readText(prompt)); } catch (NumberFormatException e) { System.out.println("Please enter a valid whole number."); } }
    }
    private static double readDouble(String prompt) {
        while (true) { try { return Double.parseDouble(readText(prompt)); } catch (NumberFormatException e) { System.out.println("Please enter a valid number."); } }
    }
    private static void showError(Exception e) { System.out.println("Error: " + e.getMessage()); }
}
