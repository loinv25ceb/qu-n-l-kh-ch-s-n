package com.hotel.server.dao;

import com.hotel.server.model.Room;
import com.hotel.server.util.DatabaseConnection;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RoomDAO {
    
    /**
     * Find room by ID
     */
    public Room findById(int roomId) {
        String sql = "SELECT r.*, rt.type_name FROM rooms r " +
                     "JOIN room_types rt ON r.room_type_id = rt.room_type_id " +
                     "WHERE r.room_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, roomId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToRoom(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error finding room: " + e.getMessage());
        }
        return null;
    }

    /**
     * Find room by room number
     */
    public Room findByRoomNumber(String roomNumber) {
        String sql = "SELECT r.*, rt.type_name FROM rooms r " +
                     "JOIN room_types rt ON r.room_type_id = rt.room_type_id " +
                     "WHERE r.room_number = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, roomNumber);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToRoom(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error finding room: " + e.getMessage());
        }
        return null;
    }

    /**
     * Get all rooms
     */
    public List<Room> findAll() {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT r.*, rt.type_name FROM rooms r " +
                     "JOIN room_types rt ON r.room_type_id = rt.room_type_id " +
                     "ORDER BY r.room_number";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                rooms.add(mapResultSetToRoom(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting all rooms: " + e.getMessage());
        }
        return rooms;
    }

    /**
     * Get available rooms
     */
    public List<Room> findAvailableRooms() {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT r.*, rt.type_name FROM rooms r " +
                     "JOIN room_types rt ON r.room_type_id = rt.room_type_id " +
                     "WHERE r.status = 'AVAILABLE' ORDER BY r.room_number";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                rooms.add(mapResultSetToRoom(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting available rooms: " + e.getMessage());
        }
        return rooms;
    }

    /**
     * Get rooms by status
     */
    public List<Room> findByStatus(String status) {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT r.*, rt.type_name FROM rooms r " +
                     "JOIN room_types rt ON r.room_type_id = rt.room_type_id " +
                     "WHERE r.status = ? ORDER BY r.room_number";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                rooms.add(mapResultSetToRoom(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding rooms by status: " + e.getMessage());
        }
        return rooms;
    }

    /**
     * Insert new room
     */
    public boolean insert(Room room) {
        String sql = "INSERT INTO rooms (room_number, room_type_id, floor, status, description, amenities) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, room.getRoomNumber());
            pstmt.setInt(2, room.getRoomTypeId());
            pstmt.setInt(3, room.getFloor());
            pstmt.setString(4, room.getStatus());
            pstmt.setString(5, room.getDescription());
            pstmt.setString(6, room.getAmenities());
            
            int rowsInserted = pstmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            System.err.println("Error inserting room: " + e.getMessage());
            return false;
        }
    }

    /**
     * Update room
     */
    public boolean update(Room room) {
        String sql = "UPDATE rooms SET room_number=?, room_type_id=?, floor=?, status=?, description=?, amenities=? " +
                     "WHERE room_id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, room.getRoomNumber());
            pstmt.setInt(2, room.getRoomTypeId());
            pstmt.setInt(3, room.getFloor());
            pstmt.setString(4, room.getStatus());
            pstmt.setString(5, room.getDescription());
            pstmt.setString(6, room.getAmenities());
            pstmt.setInt(7, room.getRoomId());
            
            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.err.println("Error updating room: " + e.getMessage());
            return false;
        }
    }

    /**
     * Update room status
     */
    public boolean updateStatus(int roomId, String status) {
        String sql = "UPDATE rooms SET status=? WHERE room_id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status);
            pstmt.setInt(2, roomId);
            
            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.err.println("Error updating room status: " + e.getMessage());
            return false;
        }
    }

    /**
     * Delete room
     */
    public boolean delete(int roomId) {
        String sql = "DELETE FROM rooms WHERE room_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, roomId);
            int rowsDeleted = pstmt.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting room: " + e.getMessage());
            return false;
        }
    }

    /**
     * Search rooms
     */
    public List<Room> search(String keyword) {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT r.*, rt.type_name FROM rooms r " +
                     "JOIN room_types rt ON r.room_type_id = rt.room_type_id " +
                     "WHERE r.room_number LIKE ? OR rt.type_name LIKE ? " +
                     "ORDER BY r.room_number";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + keyword + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                rooms.add(mapResultSetToRoom(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error searching rooms: " + e.getMessage());
        }
        return rooms;
    }

    /**
     * Map ResultSet to Room object
     */
    private Room mapResultSetToRoom(ResultSet rs) throws SQLException {
        Room room = new Room();
        room.setRoomId(rs.getInt("room_id"));
        room.setRoomNumber(rs.getString("room_number"));
        room.setRoomTypeId(rs.getInt("room_type_id"));
        room.setRoomTypeName(rs.getString("type_name"));
        room.setFloor(rs.getInt("floor"));
        room.setStatus(rs.getString("status"));
        room.setDescription(rs.getString("description"));
        room.setAmenities(rs.getString("amenities"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            room.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            room.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        
        return room;
    }
}
