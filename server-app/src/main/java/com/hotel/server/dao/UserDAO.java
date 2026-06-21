package com.hotel.server.dao;

import com.hotel.server.model.User;
import com.hotel.server.util.DatabaseConnection;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    
    /**
     * Find user by username
     */
    public User findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error finding user: " + e.getMessage());
        }
        return null;
    }

    /**
     * Find user by ID
     */
    public User findById(int userId) {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error finding user: " + e.getMessage());
        }
        return null;
    }

    /**
     * Get all users
     */
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users ORDER BY user_id";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting all users: " + e.getMessage());
        }
        return users;
    }

    /**
     * Insert new user
     */
    public boolean insert(User user) {
        String sql = "INSERT INTO users (username, password_hash, email, full_name, role, phone, address, is_active) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPasswordHash());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getFullName());
            pstmt.setString(5, user.getRole());
            pstmt.setString(6, user.getPhone());
            pstmt.setString(7, user.getAddress());
            pstmt.setBoolean(8, user.isActive());
            
            int rowsInserted = pstmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            System.err.println("Error inserting user: " + e.getMessage());
            return false;
        }
    }

    /**
     * Update user
     */
    public boolean update(User user) {
        String sql = "UPDATE users SET username=?, email=?, full_name=?, role=?, phone=?, address=?, is_active=? WHERE user_id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getFullName());
            pstmt.setString(4, user.getRole());
            pstmt.setString(5, user.getPhone());
            pstmt.setString(6, user.getAddress());
            pstmt.setBoolean(7, user.isActive());
            pstmt.setInt(8, user.getUserId());
            
            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.err.println("Error updating user: " + e.getMessage());
            return false;
        }
    }

    /**
     * Delete user
     */
    public boolean delete(int userId) {
        String sql = "UPDATE users SET is_active = FALSE WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting user: " + e.getMessage());
            return false;
        }
    }

    /**
     * Update failed login attempts
     */
    public void updateFailedLoginAttempts(int userId, int attempts) {
        String sql = "UPDATE users SET failed_login_attempts = ? WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, attempts);
            pstmt.setInt(2, userId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating failed login attempts: " + e.getMessage());
        }
    }

    /**
     * Lock user account
     */
    public void lockAccount(int userId, LocalDateTime lockedUntil) {
        String sql = "UPDATE users SET locked_until = ? WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setTimestamp(1, Timestamp.valueOf(lockedUntil));
            pstmt.setInt(2, userId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error locking account: " + e.getMessage());
        }
    }

    /**
     * Search users by name or email
     */
    public List<User> search(String keyword) {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE full_name LIKE ? OR email LIKE ? OR username LIKE ? ORDER BY user_id";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + keyword + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error searching users: " + e.getMessage());
        }
        return users;
    }

    /**
     * Map ResultSet to User object
     */
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getInt("user_id"));
        user.setUsername(rs.getString("username"));
        user.setPasswordHash(rs.getString("password_hash"));
        user.setEmail(rs.getString("email"));
        user.setFullName(rs.getString("full_name"));
        user.setRole(rs.getString("role"));
        user.setPhone(rs.getString("phone"));
        user.setAddress(rs.getString("address"));
        user.setActive(rs.getBoolean("is_active"));
        user.setFailedLoginAttempts(rs.getInt("failed_login_attempts"));
        
        Timestamp lockedUntil = rs.getTimestamp("locked_until");
        if (lockedUntil != null) {
            user.setLockedUntil(lockedUntil.toLocalDateTime());
        }
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            user.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            user.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        
        return user;
    }
}
