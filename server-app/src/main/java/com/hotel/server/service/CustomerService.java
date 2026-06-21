package com.hotel.server.service;

import com.hotel.server.dao.CustomerDAO;
import com.hotel.server.model.Customer;
import com.hotel.server.security.AESUtil;
import com.hotel.server.util.LogUtil;
import java.util.List;

public class CustomerService {
    private CustomerDAO customerDAO = new CustomerDAO();

    /**
     * Get customer by ID
     */
    public Customer getCustomerById(int customerId) {
        Customer customer = customerDAO.findById(customerId);
        decryptCustomerData(customer);
        return customer;
    }

    /**
     * Get all customers
     */
    public List<Customer> getAllCustomers() {
        List<Customer> customers = customerDAO.findAll();
        customers.forEach(this::decryptCustomerData);
        return customers;
    }

    /**
     * Create new customer
     */
    public boolean createCustomer(Customer customer) {
        encryptCustomerData(customer);
        boolean result = customerDAO.insert(customer);
        if (result) {
            LogUtil.logActivity(null, LogUtil.ActionType.CREATE, LogUtil.ModuleType.BOOKING,
                    "Customer created: " + customer.getFullName());
        }
        return result;
    }

    /**
     * Update customer
     */
    public boolean updateCustomer(Customer customer) {
        encryptCustomerData(customer);
        boolean result = customerDAO.update(customer);
        if (result) {
            LogUtil.logActivity(null, LogUtil.ActionType.UPDATE, LogUtil.ModuleType.BOOKING,
                    customer.getCustomerId(), "Customer updated", null, null);
        }
        return result;
    }

    /**
     * Delete customer
     */
    public boolean deleteCustomer(int customerId) {
        boolean result = customerDAO.delete(customerId);
        if (result) {
            LogUtil.logActivity(null, LogUtil.ActionType.DELETE, LogUtil.ModuleType.BOOKING,
                    customerId, "Customer deleted", null, null);
        }
        return result;
    }

    /**
     * Search customers
     */
    public List<Customer> searchCustomers(String keyword) {
        List<Customer> customers = customerDAO.search(keyword);
        customers.forEach(this::decryptCustomerData);
        return customers;
    }

    /**
     * Encrypt sensitive customer data
     */
    private void encryptCustomerData(Customer customer) {
        if (customer.getPhone() != null && !customer.getPhone().isEmpty()) {
            customer.setPhone(AESUtil.encrypt(customer.getPhone()));
        }
        if (customer.getAddress() != null && !customer.getAddress().isEmpty()) {
            customer.setAddress(AESUtil.encrypt(customer.getAddress()));
        }
        if (customer.getIdNumber() != null && !customer.getIdNumber().isEmpty()) {
            customer.setIdNumber(AESUtil.encrypt(customer.getIdNumber()));
        }
    }

    /**
     * Decrypt sensitive customer data
     */
    private void decryptCustomerData(Customer customer) {
        if (customer == null) return;
        
        if (customer.getPhone() != null && !customer.getPhone().isEmpty()) {
            customer.setPhone(AESUtil.decrypt(customer.getPhone()));
        }
        if (customer.getAddress() != null && !customer.getAddress().isEmpty()) {
            customer.setAddress(AESUtil.decrypt(customer.getAddress()));
        }
        if (customer.getIdNumber() != null && !customer.getIdNumber().isEmpty()) {
            customer.setIdNumber(AESUtil.decrypt(customer.getIdNumber()));
        }
    }
}
