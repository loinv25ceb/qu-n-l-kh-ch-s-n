package com.hotel.server.util;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class LogUtil {
    private static final String LOG_FILE = "logs/system.log";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public enum ActionType {
        LOGIN, LOGOUT, CREATE, UPDATE, DELETE, EXPORT, IMPORT, SEARCH, VIEW
    }

    public enum ModuleType {
        USER, ROOM, CUSTOMER, BOOKING, SERVICE, INVOICE, SYSTEM
    }

    /**
     * Log activity to file
     */
    public static synchronized void logActivity(
            Integer userId,
            ActionType actionType,
            ModuleType moduleType,
            String description) {
        logActivity(userId, actionType, moduleType, null, description, null, null);
    }

    /**
     * Log activity with details
     */
    public static synchronized void logActivity(
            Integer userId,
            ActionType actionType,
            ModuleType moduleType,
            Integer recordId,
            String description,
            Map<String, Object> oldValues,
            Map<String, Object> newValues) {
        try {
            String timestamp = LocalDateTime.now().format(FORMATTER);
            String logEntry = formatLogEntry(timestamp, userId, actionType, moduleType, recordId, description);
            
            // Write to file
            try (FileWriter writer = new FileWriter(LOG_FILE, true)) {
                writer.write(logEntry + "\n");
                writer.flush();
            }
            
            // Also print to console
            System.out.println(logEntry);
        } catch (IOException e) {
            System.err.println("Error writing to log: " + e.getMessage());
        }
    }

    /**
     * Log error
     */
    public static void logError(String errorMessage, Exception e) {
        try {
            String timestamp = LocalDateTime.now().format(FORMATTER);
            String logEntry = String.format("%s | ERROR | %s | %s",
                    timestamp,
                    errorMessage,
                    e != null ? e.getMessage() : "No exception details");
            
            try (FileWriter writer = new FileWriter(LOG_FILE, true)) {
                writer.write(logEntry + "\n");
                writer.flush();
            }
            
            System.err.println(logEntry);
        } catch (IOException ex) {
            System.err.println("Error writing error log: " + ex.getMessage());
        }
    }

    /**
     * Format log entry
     */
    private static String formatLogEntry(
            String timestamp,
            Integer userId,
            ActionType actionType,
            ModuleType moduleType,
            Integer recordId,
            String description) {
        return String.format("%s | USER:%s | %s | %s | RECORD:%s | %s",
                timestamp,
                userId != null ? userId : "SYSTEM",
                actionType.name(),
                moduleType.name(),
                recordId != null ? recordId : "N/A",
                description != null ? description : "");
    }
}
