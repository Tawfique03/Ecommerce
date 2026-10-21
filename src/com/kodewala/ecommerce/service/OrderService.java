package com.kodewala.ecommerce.service;

import java.util.ArrayList;
import java.util.List;
import com.kodewala.ecommerce.exception.CustomerNotFoundException;
import com.kodewala.ecommerce.exception.InsufficientStockException;
import com.kodewala.ecommerce.exception.InvalidOrderException;
import com.kodewala.ecommerce.model.CartItem;
import com.kodewala.ecommerce.model.Customer;
import com.kodewala.ecommerce.model.Order;
import com.kodewala.ecommerce.model.OrderStatus;
import com.kodewala.ecommerce.model.Product;
import com.kodewala.ecommerce.repository.OrderRepository;

public class OrderService {
    private OrderRepository orderRepository;
    private CustomerService customerService;
    private ProductService productService;
    private CartService cartService;
    private int nextOrderId = 5001;

    public OrderService(OrderRepository orderRepository, CustomerService customerService,
            ProductService productService, CartService cartService) {
        this.orderRepository = orderRepository; this.customerService = customerService;
        this.productService = productService; this.cartService = cartService;
    }
    public Order placeOrder(int customerId) throws CustomerNotFoundException, InvalidOrderException, InsufficientStockException {
        customerService.findById(customerId);
        List<CartItem> cart = cartService.getCart(customerId);
        if (cart.isEmpty()) throw new InvalidOrderException("Your cart is empty.");
        for (CartItem item : cart) {
            Product product;
            try { product = productService.findById(item.getProductId()); }
            catch (Exception e) { throw new InvalidOrderException("A product in your cart no longer exists."); }
            if (product.getQuantity() < item.getQuantity()) throw new InsufficientStockException("Not enough stock for " + product.getProductName());
        }
        double total = cartService.getTotal(customerId);
        Order order = new Order(nextOrderId++, customerId, new ArrayList<>(cart), total);
        for (CartItem item : cart) {
            try {
                Product product = productService.findById(item.getProductId());
                product.setQuantity(product.getQuantity() - item.getQuantity());
            } catch (Exception e) {
                throw new InvalidOrderException("A product in your cart could not be found.");
            }
        }
        orderRepository.addOrder(order);
        cartService.clearCart(customerId);
        System.out.println("Order placed successfully. Order ID: " + order.getOrderId());
        return order;
    }
    public List<Order> getAllOrders() { return orderRepository.getAllOrders(); }
    public List<Order> getCustomerOrders(int customerId) { return orderRepository.getOrdersByCustomer(customerId); }
    public void cancelOrder(int customerId, int orderId) throws InvalidOrderException, CustomerNotFoundException {
        customerService.findById(customerId);
        Order order = orderRepository.findById(orderId);
        if (order == null || order.getCustomerId() != customerId) throw new InvalidOrderException("Order not found for this customer.");
        if (order.getOrderStatus() == OrderStatus.CANCELLED) throw new InvalidOrderException("This order is already cancelled.");
        if (order.getOrderStatus() != OrderStatus.PLACED) throw new InvalidOrderException("Only orders with PLACED status can be cancelled.");
        for (CartItem item : order.getItems()) {
            try {
                Product product = productService.findById(item.getProductId());
                product.setQuantity(product.getQuantity() + item.getQuantity());
            } catch (Exception e) { /* Product was deleted; nothing to restock. */ }
        }
        order.setOrderStatus(OrderStatus.CANCELLED);
        System.out.println("Order cancelled and stock restored where possible.");
    }
}
