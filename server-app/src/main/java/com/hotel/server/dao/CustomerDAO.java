package com.hotel.server.dao;

import com.hotel.server.model.Customer;
import com.hotel.server.util.DatabaseConnection;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {
    
    /**
     * Find customer by ID
     */
    public Customer findById(int customerId) {
        String sql = "SELECT * FROM customers WHERE customer_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, customerId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToCustomer(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error finding customer: " + e.getMessage());
        }
        return null;
    }

    /**
     * Get all customers
     */
    public List<Customer> findAll() {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customers WHERE is_active = TRUE ORDER BY full_name";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                customers.add(mapResultSetToCustomer(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting all customers: " + e.getMessage());
        }
        return customers;
    }

    /**
     * Insert new customer
     */
    public boolean insert(Customer customer) {
        String sql = "INSERT INTO customers (full_name, id_number, phone, email, address, nationality, gender, date_of_birth, note, is_active) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, customer.getFullName());
            pstmt.setString(2, customer.getIdNumber());
            pstmt.setString(3, customer.getPhone());
            pstmt.setString(4, customer.getEmail());
            pstmt.setString(5, customer.getAddress());
            pstmt.setString(6, customer.getNationality());
            pstmt.setString(7, customer.getGender());
            
            if (customer.getDateOfBirth() != null) {
                pstmt.setDate(8, Date.valueOf(customer.getDateOfBirth()));
            } else {
                pstmt.setNull(8, Types.DATE);
            }
            
            pstmt.setString(9, customer.getNote());
            pstmt.setBoolean(10, customer.isActive());
            
            int rowsInserted = pstmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            System.err.println("Error inserting customer: " + e.getMessage());
            return false;
        }
    }

    /**
     * Update customer
     */
    public boolean update(Customer customer) {
        String sql = "UPDATE customers SET full_name=?, id_number=?, phone=?, email=?, address=?, nationality=?, gender=?, date_of_birth=?, note=? " +
                     "WHERE customer_id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, customer.getFullName());
            pstmt.setString(2, customer.getIdNumber());
            pstmt.setString(3, customer.getPhone());
            pstmt.setString(4, customer.getEmail());
            pstmt.setString(5, customer.getAddress());
            pstmt.setString(6, customer.getNationality());
            pstmt.setString(7, customer.getGender());
            
            if (customer.getDateOfBirth() != null) {
                pstmt.setDate(8, Date.valueOf(customer.getDateOfBirth()));
            } else {
                pstmt.setNull(8, Types.DATE);
            }
            
            pstmt.setString(9, customer.getNote());
            pstmt.setInt(10, customer.getCustomerId());
            
            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.err.println("Error updating customer: " + e.getMessage());
            return false;
        }
    }

    /**
     * Delete customer (soft delete)
     */
    public boolean delete(int customerId) {
        String sql = "UPDATE customers SET is_active = FALSE WHERE customer_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, customerId);
            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting customer: " + e.getMessage());
            return false;
        }
    }

    /**
     * Search customers by name, phone, or email
     */
    public List<Customer> search(String keyword) {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customers WHERE is_active = TRUE AND " +
                     "(full_name LIKE ? OR phone LIKE ? OR email LIKE ? OR id_number LIKE ?) " +
                     "ORDER BY full_name";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + keyword + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);
            pstmt.setString(4, searchPattern);
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                customers.add(mapResultSetToCustomer(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error searching customers: " + e.getMessage());
        }
        return customers;
    }

    /**
     * Map ResultSet to Customer object
     */
    private Customer mapResultSetToCustomer(ResultSet rs) throws SQLException {
        Customer customer = new Customer();
        customer.setCustomerId(rs.getInt("customer_id"));
        customer.setFullName(rs.getString("full_name"));
        customer.setIdNumber(rs.getString("id_number"));
        customer.setPhone(rs.getString("phone"));
        customer.setEmail(rs.getString("email"));
        customer.setAddress(rs.getString("address"));
        customer.setNationality(rs.getString("nationality"));
        customer.setGender(rs.getString("gender"));
        
        Date dateOfBirth = rs.getDate("date_of_birth");
        if (dateOfBirth != null) {
            customer.setDateOfBirth(dateOfBirth.toLocalDate());
        }
        
        customer.setNote(rs.getString("note"));
        customer.setActive(rs.getBoolean("is_active"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            customer.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            customer.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        
        return customer;
    }
}
