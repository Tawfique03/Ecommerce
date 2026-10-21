package com.kodewala.ecommerce.service;

import java.util.Collection;
import com.kodewala.ecommerce.exception.CustomerNotFoundException;
import com.kodewala.ecommerce.model.Customer;
import com.kodewala.ecommerce.repository.CustomerRepository;

public class CustomerService {
    private CustomerRepository customerRepository;
    private int nextCustomerId = 1001;
    public CustomerService(CustomerRepository customerRepository) { this.customerRepository = customerRepository; }

    public Customer register(String name, String email, String mobile, String address) {
        Customer customer = new Customer(nextCustomerId++, name, email, mobile, address);
        customerRepository.addCustomer(customer);
        System.out.println("Registration successful. Your customer ID is: " + customer.getCustomerId());
        return customer;
    }
    public Customer findById(int id) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(id);
        if (customer == null) throw new CustomerNotFoundException("Customer with ID " + id + " does not exist.");
        return customer;
    }
    public Collection<Customer> getAllCustomers() { return customerRepository.getAllCustomers(); }
    public void updateAddress(int id, String address) throws CustomerNotFoundException {
        findById(id).setAddress(address);
        System.out.println("Address updated.");
    }
}
