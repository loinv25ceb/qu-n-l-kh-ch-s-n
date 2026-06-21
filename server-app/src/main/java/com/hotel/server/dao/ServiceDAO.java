package com.hotel.server.dao;

import com.hotel.server.model.Service;
import com.hotel.server.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceDAO {
    
    /**
     * Find service by ID
     */
    public Service findById(int serviceId) {
        String sql = "SELECT * FROM services WHERE service_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, serviceId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToService(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error finding service: " + e.getMessage());
        }
        return null;
    }

    /**
     * Get all services
     */
    public List<Service> findAll() {
        List<Service> services = new ArrayList<>();
        String sql = "SELECT * FROM services WHERE is_active = TRUE ORDER BY service_name";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                services.add(mapResultSetToService(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting all services: " + e.getMessage());
        }
        return services;
    }

    /**
     * Get services by type
     */
    public List<Service> findByType(String serviceType) {
        List<Service> services = new ArrayList<>();
        String sql = "SELECT * FROM services WHERE service_type = ? AND is_active = TRUE ORDER BY service_name";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, serviceType);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                services.add(mapResultSetToService(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding services by type: " + e.getMessage());
        }
        return services;
    }

    /**
     * Insert new service
     */
    public boolean insert(Service service) {
        String sql = "INSERT INTO services (service_name, description, service_type, price, is_active) " +
                     "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, service.getServiceName());
            pstmt.setString(2, service.getDescription());
            pstmt.setString(3, service.getServiceType());
            pstmt.setBigDecimal(4, service.getPrice());
            pstmt.setBoolean(5, service.isActive());
            
            int rowsInserted = pstmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            System.err.println("Error inserting service: " + e.getMessage());
            return false;
        }
    }

    /**
     * Update service
     */
    public boolean update(Service service) {
        String sql = "UPDATE services SET service_name=?, description=?, service_type=?, price=?, is_active=? " +
                     "WHERE service_id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, service.getServiceName());
            pstmt.setString(2, service.getDescription());
            pstmt.setString(3, service.getServiceType());
            pstmt.setBigDecimal(4, service.getPrice());
            pstmt.setBoolean(5, service.isActive());
            pstmt.setInt(6, service.getServiceId());
            
            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.err.println("Error updating service: " + e.getMessage());
            return false;
        }
    }

    /**
     * Delete service (soft delete)
     */
    public boolean delete(int serviceId) {
        String sql = "UPDATE services SET is_active = FALSE WHERE service_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, serviceId);
            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting service: " + e.getMessage());
            return false;
        }
    }

    /**
     * Search services
     */
    public List<Service> search(String keyword) {
        List<Service> services = new ArrayList<>();
        String sql = "SELECT * FROM services WHERE is_active = TRUE AND " +
                     "(service_name LIKE ? OR description LIKE ?) " +
                     "ORDER BY service_name";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + keyword + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                services.add(mapResultSetToService(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error searching services: " + e.getMessage());
        }
        return services;
    }

    /**
     * Map ResultSet to Service object
     */
    private Service mapResultSetToService(ResultSet rs) throws SQLException {
        Service service = new Service();
        service.setServiceId(rs.getInt("service_id"));
        service.setServiceName(rs.getString("service_name"));
        service.setDescription(rs.getString("description"));
        service.setServiceType(rs.getString("service_type"));
        service.setPrice(rs.getBigDecimal("price"));
        service.setActive(rs.getBoolean("is_active"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            service.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            service.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        
        return service;
    }
}
