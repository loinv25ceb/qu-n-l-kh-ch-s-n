package com.hotel.server.model;

import java.io.Serializable;

public class Response implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private boolean success;
    private String message;
    private Object data;
    private int statusCode; // 200, 400, 401, 500...

    public Response() {}

    public Response(boolean success, String message) {
        this.success = success;
        this.message = message;
        this.statusCode = success ? 200 : 400;
    }

    public Response(boolean success, String message, Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.statusCode = success ? 200 : 400;
    }

    // Getters and Setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Object getData() { return data; }
    public void setData(Object data) { this.data = data; }

    public int getStatusCode() { return statusCode; }
    public void setStatusCode(int statusCode) { this.statusCode = statusCode; }

    @Override
    public String toString() {
        return "Response{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", statusCode=" + statusCode +
                '}';
    }
}
