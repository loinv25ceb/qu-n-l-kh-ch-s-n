package com.hotel.server.model;

import java.io.Serializable;

public class Request implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String action; // LOGIN, LOGOUT, ROOM_FIND_ALL, CUSTOMER_CREATE...
    private Object data;
    private String token; // For future authentication
    private int userId; // User making the request

    public Request() {}

    public Request(String action, Object data) {
        this.action = action;
        this.data = data;
    }

    // Getters and Setters
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public Object getData() { return data; }
    public void setData(Object data) { this.data = data; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    @Override
    public String toString() {
        return "Request{" +
                "action='" + action + '\'' +
                ", userId=" + userId +
                '}';
    }
}
