package com.hotel.server.service;

import com.hotel.server.dao.RoomDAO;
import com.hotel.server.model.Room;
import com.hotel.server.util.LogUtil;
import java.util.List;

public class RoomService {
    private RoomDAO roomDAO = new RoomDAO();

    /**
     * Get room by ID
     */
    public Room getRoomById(int roomId) {
        return roomDAO.findById(roomId);
    }

    /**
     * Get all rooms
     */
    public List<Room> getAllRooms() {
        return roomDAO.findAll();
    }

    /**
     * Get available rooms
     */
    public List<Room> getAvailableRooms() {
        return roomDAO.findAvailableRooms();
    }

    /**
     * Get rooms by status
     */
    public List<Room> getRoomsByStatus(String status) {
        return roomDAO.findByStatus(status);
    }

    /**
     * Create new room
     */
    public boolean createRoom(Room room) {
        boolean result = roomDAO.insert(room);
        if (result) {
            LogUtil.logActivity(null, LogUtil.ActionType.CREATE, LogUtil.ModuleType.BOOKING,
                    "Room created: " + room.getRoomNumber());
        }
        return result;
    }

    /**
     * Update room
     */
    public boolean updateRoom(Room room) {
        boolean result = roomDAO.update(room);
        if (result) {
            LogUtil.logActivity(null, LogUtil.ActionType.UPDATE, LogUtil.ModuleType.BOOKING,
                    room.getRoomId(), "Room updated", null, null);
        }
        return result;
    }

    /**
     * Update room status
     */
    public boolean updateRoomStatus(int roomId, String status) {
        boolean result = roomDAO.updateStatus(roomId, status);
        if (result) {
            LogUtil.logActivity(null, LogUtil.ActionType.UPDATE, LogUtil.ModuleType.BOOKING,
                    roomId, "Room status updated to: " + status, null, null);
        }
        return result;
    }

    /**
     * Delete room
     */
    public boolean deleteRoom(int roomId) {
        boolean result = roomDAO.delete(roomId);
        if (result) {
            LogUtil.logActivity(null, LogUtil.ActionType.DELETE, LogUtil.ModuleType.BOOKING,
                    roomId, "Room deleted", null, null);
        }
        return result;
    }

    /**
     * Search rooms
     */
    public List<Room> searchRooms(String keyword) {
        return roomDAO.search(keyword);
    }
}
