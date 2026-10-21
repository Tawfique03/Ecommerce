package com.kodewala.ecommerce.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import com.kodewala.ecommerce.model.Order;

public class OrderRepository {
    private List<Order> orders = new ArrayList<>();
    public void addOrder(Order order) { orders.add(order); }
    public List<Order> getAllOrders() { return orders; }
    public Order findById(int id) {
        for (Order order : orders) if (order.getOrderId() == id) return order;
        return null;
    }
    public List<Order> getOrdersByCustomer(int customerId) {
        return orders.stream().filter(o -> o.getCustomerId() == customerId).collect(Collectors.toList());
    }
}
