package com.kodewala.ecommerce.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Collection;
import com.kodewala.ecommerce.model.Customer;

public class CustomerRepository {
    private Map<Integer, Customer> customers = new HashMap<>();

    public void addCustomer(Customer customer) { customers.put(customer.getCustomerId(), customer); }
    public Customer findById(int id) { return customers.get(id); }
    public Collection<Customer> getAllCustomers() { return customers.values(); }
    public boolean containsId(int id) { return customers.containsKey(id); }
}
