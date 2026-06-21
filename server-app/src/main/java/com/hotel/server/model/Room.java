package com.hotel.server.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Room implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int roomId;
    private String roomNumber;
    private int roomTypeId;
    private String roomTypeName; // For display
    private int floor;
    private String status; // AVAILABLE, OCCUPIED, MAINTENANCE, RESERVED
    private String description;
    private String amenities; // JSON format
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Room() {}

    public Room(String roomNumber, int roomTypeId, int floor, String status) {
        this.roomNumber = roomNumber;
        this.roomTypeId = roomTypeId;
        this.floor = floor;
        this.status = status;
    }

    // Getters and Setters
    public int getRoomId() { return roomId; }
    public void setRoomId(int roomId) { this.roomId = roomId; }

    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }

    public int getRoomTypeId() { return roomTypeId; }
    public void setRoomTypeId(int roomTypeId) { this.roomTypeId = roomTypeId; }

    public String getRoomTypeName() { return roomTypeName; }
    public void setRoomTypeName(String roomTypeName) { this.roomTypeName = roomTypeName; }

    public int getFloor() { return floor; }
    public void setFloor(int floor) { this.floor = floor; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getAmenities() { return amenities; }
    public void setAmenities(String amenities) { this.amenities = amenities; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public String toString() {
        return "Room{" +
                "roomId=" + roomId +
                ", roomNumber='" + roomNumber + '\'' +
                ", roomTypeName='" + roomTypeName + '\'' +
                ", floor=" + floor +
                ", status='" + status + '\'' +
                '}';
    }
}
