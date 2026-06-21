package com.hotel.server.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Booking implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int bookingId;
    private String bookingNumber;
    private int customerId;
    private String customerName; // For display
    private int roomId;
    private String roomNumber; // For display
    private LocalDateTime checkInDate;
    private LocalDateTime checkOutDate;
    private int numberOfGuests;
    private int totalNights;
    private BigDecimal roomRate;
    private String status; // PENDING, CONFIRMED, CHECKED_IN, CHECKED_OUT, CANCELLED
    private String specialRequest;
    private int createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Booking() {}

    public Booking(int customerId, int roomId, LocalDateTime checkInDate, LocalDateTime checkOutDate) {
        this.customerId = customerId;
        this.roomId = roomId;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.status = "PENDING";
    }

    // Getters and Setters
    public int getBookingId() { return bookingId; }
    public void setBookingId(int bookingId) { this.bookingId = bookingId; }

    public String getBookingNumber() { return bookingNumber; }
    public void setBookingNumber(String bookingNumber) { this.bookingNumber = bookingNumber; }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public int getRoomId() { return roomId; }
    public void setRoomId(int roomId) { this.roomId = roomId; }

    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }

    public LocalDateTime getCheckInDate() { return checkInDate; }
    public void setCheckInDate(LocalDateTime checkInDate) { this.checkInDate = checkInDate; }

    public LocalDateTime getCheckOutDate() { return checkOutDate; }
    public void setCheckOutDate(LocalDateTime checkOutDate) { this.checkOutDate = checkOutDate; }

    public int getNumberOfGuests() { return numberOfGuests; }
    public void setNumberOfGuests(int numberOfGuests) { this.numberOfGuests = numberOfGuests; }

    public int getTotalNights() { return totalNights; }
    public void setTotalNights(int totalNights) { this.totalNights = totalNights; }

    public BigDecimal getRoomRate() { return roomRate; }
    public void setRoomRate(BigDecimal roomRate) { this.roomRate = roomRate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getSpecialRequest() { return specialRequest; }
    public void setSpecialRequest(String specialRequest) { this.specialRequest = specialRequest; }

    public int getCreatedBy() { return createdBy; }
    public void setCreatedBy(int createdBy) { this.createdBy = createdBy; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public String toString() {
        return "Booking{" +
                "bookingId=" + bookingId +
                ", bookingNumber='" + bookingNumber + '\'' +
                ", customerName='" + customerName + '\'' +
                ", roomNumber='" + roomNumber + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
