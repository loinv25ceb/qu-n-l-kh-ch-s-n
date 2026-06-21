package com.hotel.server.dao;

import com.hotel.server.model.Invoice;
import com.hotel.server.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InvoiceDAO {
    
    /**
     * Find invoice by ID
     */
    public Invoice findById(int invoiceId) {
        String sql = "SELECT i.*, c.full_name as customer_name FROM invoices i " +
                     "JOIN customers c ON i.customer_id = c.customer_id " +
                     "WHERE i.invoice_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, invoiceId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToInvoice(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error finding invoice: " + e.getMessage());
        }
        return null;
    }

    /**
     * Get all invoices
     */
    public List<Invoice> findAll() {
        List<Invoice> invoices = new ArrayList<>();
        String sql = "SELECT i.*, c.full_name as customer_name FROM invoices i " +
                     "JOIN customers c ON i.customer_id = c.customer_id " +
                     "ORDER BY i.invoice_date DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                invoices.add(mapResultSetToInvoice(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting all invoices: " + e.getMessage());
        }
        return invoices;
    }

    /**
     * Find invoice by booking ID
     */
    public Invoice findByBookingId(int bookingId) {
        String sql = "SELECT i.*, c.full_name as customer_name FROM invoices i " +
                     "JOIN customers c ON i.customer_id = c.customer_id " +
                     "WHERE i.booking_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, bookingId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToInvoice(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error finding invoice by booking: " + e.getMessage());
        }
        return null;
    }

    /**
     * Insert new invoice
     */
    public int insert(Invoice invoice) {
        String sql = "INSERT INTO invoices (invoice_number, booking_id, customer_id, room_charges, " +
                     "service_charges, discount, tax_rate, tax_amount, total_amount, " +
                     "payment_status, payment_method, paid_amount, remaining_amount, note, created_by) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, invoice.getInvoiceNumber());
            pstmt.setInt(2, invoice.getBookingId());
            pstmt.setInt(3, invoice.getCustomerId());
            pstmt.setBigDecimal(4, invoice.getRoomCharges());
            pstmt.setBigDecimal(5, invoice.getServiceCharges());
            pstmt.setBigDecimal(6, invoice.getDiscount());
            pstmt.setBigDecimal(7, invoice.getTaxRate());
            pstmt.setBigDecimal(8, invoice.getTaxAmount());
            pstmt.setBigDecimal(9, invoice.getTotalAmount());
            pstmt.setString(10, invoice.getPaymentStatus());
            pstmt.setString(11, invoice.getPaymentMethod());
            pstmt.setBigDecimal(12, invoice.getPaidAmount());
            pstmt.setBigDecimal(13, invoice.getRemainingAmount());
            pstmt.setString(14, invoice.getNote());
            pstmt.setInt(15, invoice.getCreatedBy());
            
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
            System.err.println("Error inserting invoice: " + e.getMessage());
            return -1;
        }
    }

    /**
     * Update invoice
     */
    public boolean update(Invoice invoice) {
        String sql = "UPDATE invoices SET payment_status=?, payment_method=?, paid_amount=?, " +
                     "remaining_amount=?, payment_date=?, note=? WHERE invoice_id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, invoice.getPaymentStatus());
            pstmt.setString(2, invoice.getPaymentMethod());
            pstmt.setBigDecimal(3, invoice.getPaidAmount());
            pstmt.setBigDecimal(4, invoice.getRemainingAmount());
            
            if (invoice.getPaymentDate() != null) {
                pstmt.setTimestamp(5, Timestamp.valueOf(invoice.getPaymentDate()));
            } else {
                pstmt.setNull(5, Types.TIMESTAMP);
            }
            
            pstmt.setString(6, invoice.getNote());
            pstmt.setInt(7, invoice.getInvoiceId());
            
            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.err.println("Error updating invoice: " + e.getMessage());
            return false;
        }
    }

    /**
     * Search invoices
     */
    public List<Invoice> search(String keyword) {
        List<Invoice> invoices = new ArrayList<>();
        String sql = "SELECT i.*, c.full_name as customer_name FROM invoices i " +
                     "JOIN customers c ON i.customer_id = c.customer_id " +
                     "WHERE i.invoice_number LIKE ? OR c.full_name LIKE ? " +
                     "ORDER BY i.invoice_date DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + keyword + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                invoices.add(mapResultSetToInvoice(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error searching invoices: " + e.getMessage());
        }
        return invoices;
    }

    /**
     * Map ResultSet to Invoice object
     */
    private Invoice mapResultSetToInvoice(ResultSet rs) throws SQLException {
        Invoice invoice = new Invoice();
        invoice.setInvoiceId(rs.getInt("invoice_id"));
        invoice.setInvoiceNumber(rs.getString("invoice_number"));
        invoice.setBookingId(rs.getInt("booking_id"));
        invoice.setCustomerId(rs.getInt("customer_id"));
        invoice.setCustomerName(rs.getString("customer_name"));
        invoice.setRoomCharges(rs.getBigDecimal("room_charges"));
        invoice.setServiceCharges(rs.getBigDecimal("service_charges"));
        invoice.setDiscount(rs.getBigDecimal("discount"));
        invoice.setTaxRate(rs.getBigDecimal("tax_rate"));
        invoice.setTaxAmount(rs.getBigDecimal("tax_amount"));
        invoice.setTotalAmount(rs.getBigDecimal("total_amount"));
        invoice.setPaymentStatus(rs.getString("payment_status"));
        invoice.setPaymentMethod(rs.getString("payment_method"));
        invoice.setPaidAmount(rs.getBigDecimal("paid_amount"));
        invoice.setRemainingAmount(rs.getBigDecimal("remaining_amount"));
        
        Timestamp invoiceDate = rs.getTimestamp("invoice_date");
        if (invoiceDate != null) {
            invoice.setInvoiceDate(invoiceDate.toLocalDateTime());
        }
        
        Timestamp paymentDate = rs.getTimestamp("payment_date");
        if (paymentDate != null) {
            invoice.setPaymentDate(paymentDate.toLocalDateTime());
        }
        
        invoice.setNote(rs.getString("note"));
        invoice.setCreatedBy(rs.getInt("created_by"));
        
        return invoice;
    }
}
