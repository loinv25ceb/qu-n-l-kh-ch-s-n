package com.hotel.server.dao;

import com.hotel.server.model.Booking;
import com.hotel.server.util.DatabaseConnection;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {
    
    /**
     * Find booking by ID
     */
    public Booking findById(int bookingId) {
        String sql = "SELECT b.*, c.full_name as customer_name, r.room_number " +
                     "FROM bookings b " +
                     "JOIN customers c ON b.customer_id = c.customer_id " +
                     "JOIN rooms r ON b.room_id = r.room_id " +
                     "WHERE b.booking_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, bookingId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToBooking(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error finding booking: " + e.getMessage());
        }
        return null;
    }

    /**
     * Get all bookings
     */
    public List<Booking> findAll() {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT b.*, c.full_name as customer_name, r.room_number " +
                     "FROM bookings b " +
                     "JOIN customers c ON b.customer_id = c.customer_id " +
                     "JOIN rooms r ON b.room_id = r.room_id " +
                     "ORDER BY b.check_in_date DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                bookings.add(mapResultSetToBooking(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting all bookings: " + e.getMessage());
        }
        return bookings;
    }

    /**
     * Get active bookings (CHECKED_IN or PENDING)
     */
    public List<Booking> findActiveBookings() {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT b.*, c.full_name as customer_name, r.room_number " +
                     "FROM bookings b " +
                     "JOIN customers c ON b.customer_id = c.customer_id " +
                     "JOIN rooms r ON b.room_id = r.room_id " +
                     "WHERE b.status IN ('PENDING', 'CONFIRMED', 'CHECKED_IN') " +
                     "ORDER BY b.check_in_date";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                bookings.add(mapResultSetToBooking(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting active bookings: " + e.getMessage());
        }
        return bookings;
    }

    /**
     * Insert new booking
     */
    public int insert(Booking booking) {
        String sql = "INSERT INTO bookings (booking_number, customer_id, room_id, check_in_date, check_out_date, " +
                     "number_of_guests, total_nights, room_rate, status, special_request, created_by) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, booking.getBookingNumber());
            pstmt.setInt(2, booking.getCustomerId());
            pstmt.setInt(3, booking.getRoomId());
            pstmt.setTimestamp(4, Timestamp.valueOf(booking.getCheckInDate()));
            pstmt.setTimestamp(5, Timestamp.valueOf(booking.getCheckOutDate()));
            pstmt.setInt(6, booking.getNumberOfGuests());
            pstmt.setInt(7, booking.getTotalNights());
            pstmt.setBigDecimal(8, booking.getRoomRate());
            pstmt.setString(9, booking.getStatus());
            pstmt.setString(10, booking.getSpecialRequest());
            pstmt.setInt(11, booking.getCreatedBy());
            
            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
            return -1;
        } catch (SQLException e) {
            System.err.println("Error inserting booking: " + e.getMessage());
            return -1;
        }
    }

    /**
     * Update booking
     */
    public boolean update(Booking booking) {
        String sql = "UPDATE bookings SET customer_id=?, room_id=?, check_in_date=?, check_out_date=?, " +
                     "number_of_guests=?, total_nights=?, room_rate=?, status=?, special_request=? " +
                     "WHERE booking_id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, booking.getCustomerId());
            pstmt.setInt(2, booking.getRoomId());
            pstmt.setTimestamp(3, Timestamp.valueOf(booking.getCheckInDate()));
            pstmt.setTimestamp(4, Timestamp.valueOf(booking.getCheckOutDate()));
            pstmt.setInt(5, booking.getNumberOfGuests());
            pstmt.setInt(6, booking.getTotalNights());
            pstmt.setBigDecimal(7, booking.getRoomRate());
            pstmt.setString(8, booking.getStatus());
            pstmt.setString(9, booking.getSpecialRequest());
            pstmt.setInt(10, booking.getBookingId());
            
            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.err.println("Error updating booking: " + e.getMessage());
            return false;
        }
    }

    /**
     * Update booking status
     */
    public boolean updateStatus(int bookingId, String status) {
        String sql = "UPDATE bookings SET status=? WHERE booking_id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status);
            pstmt.setInt(2, bookingId);
            
            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.err.println("Error updating booking status: " + e.getMessage());
            return false;
        }
    }

    /**
     * Search bookings
     */
    public List<Booking> search(String keyword) {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT b.*, c.full_name as customer_name, r.room_number " +
                     "FROM bookings b " +
                     "JOIN customers c ON b.customer_id = c.customer_id " +
                     "JOIN rooms r ON b.room_id = r.room_id " +
                     "WHERE b.booking_number LIKE ? OR c.full_name LIKE ? OR r.room_number LIKE ? " +
                     "ORDER BY b.check_in_date DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + keyword + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                bookings.add(mapResultSetToBooking(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error searching bookings: " + e.getMessage());
        }
        return bookings;
    }

    /**
     * Map ResultSet to Booking object
     */
    private Booking mapResultSetToBooking(ResultSet rs) throws SQLException {
        Booking booking = new Booking();
        booking.setBookingId(rs.getInt("booking_id"));
        booking.setBookingNumber(rs.getString("booking_number"));
        booking.setCustomerId(rs.getInt("customer_id"));
        booking.setCustomerName(rs.getString("customer_name"));
        booking.setRoomId(rs.getInt("room_id"));
        booking.setRoomNumber(rs.getString("room_number"));
        
        Timestamp checkInDate = rs.getTimestamp("check_in_date");
        if (checkInDate != null) {
            booking.setCheckInDate(checkInDate.toLocalDateTime());
        }
        
        Timestamp checkOutDate = rs.getTimestamp("check_out_date");
        if (checkOutDate != null) {
            booking.setCheckOutDate(checkOutDate.toLocalDateTime());
        }
        
        booking.setNumberOfGuests(rs.getInt("number_of_guests"));
        booking.setTotalNights(rs.getInt("total_nights"));
        booking.setRoomRate(rs.getBigDecimal("room_rate"));
        booking.setStatus(rs.getString("status"));
        booking.setSpecialRequest(rs.getString("special_request"));
        booking.setCreatedBy(rs.getInt("created_by"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            booking.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            booking.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        
        return booking;
    }
}
