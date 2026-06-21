package com.hotel.server.service;

import com.hotel.server.dao.BookingDAO;
import com.hotel.server.dao.RoomDAO;
import com.hotel.server.model.Booking;
import com.hotel.server.model.Room;
import com.hotel.server.util.LogUtil;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class BookingService {
    private BookingDAO bookingDAO = new BookingDAO();
    private RoomDAO roomDAO = new RoomDAO();

    /**
     * Create new booking (with transaction)
     */
    public int createBooking(Booking booking) {
        try {
            // Generate booking number
            String bookingNumber = "BK-" + System.currentTimeMillis();
            booking.setBookingNumber(bookingNumber);

            // Insert booking
            int bookingId = bookingDAO.insert(booking);
            if (bookingId > 0) {
                // Update room status to RESERVED
                Room room = roomDAO.findById(booking.getRoomId());
                if (room != null) {
                    room.setStatus("RESERVED");
                    roomDAO.updateStatus(booking.getRoomId(), "RESERVED");
                }

                LogUtil.logActivity(booking.getCreatedBy(), LogUtil.ActionType.CREATE,
                        LogUtil.ModuleType.BOOKING, bookingId, "Booking created", null, null);
                return bookingId;
            }
        } catch (Exception e) {
            LogUtil.logError("Error creating booking", e);
        }
        return -1;
    }

    /**
     * Get booking by ID
     */
    public Booking getBookingById(int bookingId) {
        return bookingDAO.findById(bookingId);
    }

    /**
     * Get all bookings
     */
    public List<Booking> getAllBookings() {
        return bookingDAO.findAll();
    }

    /**
     * Get active bookings
     */
    public List<Booking> getActiveBookings() {
        return bookingDAO.findActiveBookings();
    }

    /**
     * Update booking
     */
    public boolean updateBooking(Booking booking) {
        boolean result = bookingDAO.update(booking);
        if (result) {
            LogUtil.logActivity(null, LogUtil.ActionType.UPDATE, LogUtil.ModuleType.BOOKING,
                    booking.getBookingId(), "Booking updated", null, null);
        }
        return result;
    }

    /**
     * Check-in (update status and room status)
     */
    public boolean checkIn(int bookingId) {
        Booking booking = bookingDAO.findById(bookingId);
        if (booking == null) {
            return false;
        }

        booking.setStatus("CHECKED_IN");
        boolean bookingUpdated = bookingDAO.updateStatus(bookingId, "CHECKED_IN");
        boolean roomUpdated = roomDAO.updateStatus(booking.getRoomId(), "OCCUPIED");

        if (bookingUpdated && roomUpdated) {
            LogUtil.logActivity(null, LogUtil.ActionType.UPDATE, LogUtil.ModuleType.BOOKING,
                    bookingId, "Check-in completed", null, null);
            return true;
        }
        return false;
    }

    /**
     * Check-out (update status and room status)
     */
    public boolean checkOut(int bookingId) {
        Booking booking = bookingDAO.findById(bookingId);
        if (booking == null) {
            return false;
        }

        booking.setStatus("CHECKED_OUT");
        boolean bookingUpdated = bookingDAO.updateStatus(bookingId, "CHECKED_OUT");
        boolean roomUpdated = roomDAO.updateStatus(booking.getRoomId(), "AVAILABLE");

        if (bookingUpdated && roomUpdated) {
            LogUtil.logActivity(null, LogUtil.ActionType.UPDATE, LogUtil.ModuleType.BOOKING,
                    bookingId, "Check-out completed", null, null);
            return true;
        }
        return false;
    }

    /**
     * Cancel booking
     */
    public boolean cancelBooking(int bookingId) {
        Booking booking = bookingDAO.findById(bookingId);
        if (booking == null) {
            return false;
        }

        booking.setStatus("CANCELLED");
        boolean bookingUpdated = bookingDAO.updateStatus(bookingId, "CANCELLED");
        boolean roomUpdated = roomDAO.updateStatus(booking.getRoomId(), "AVAILABLE");

        if (bookingUpdated && roomUpdated) {
            LogUtil.logActivity(null, LogUtil.ActionType.UPDATE, LogUtil.ModuleType.BOOKING,
                    bookingId, "Booking cancelled", null, null);
            return true;
        }
        return false;
    }

    /**
     * Search bookings
     */
    public List<Booking> searchBookings(String keyword) {
        return bookingDAO.search(keyword);
    }
}
