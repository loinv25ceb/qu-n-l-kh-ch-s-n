package com.hotel.server.service;

import com.hotel.server.dao.UserDAO;
import com.hotel.server.model.User;
import com.hotel.server.security.PasswordUtil;
import com.hotel.server.util.LogUtil;
import java.time.LocalDateTime;
import java.util.List;

public class UserService {
    private UserDAO userDAO = new UserDAO();
    private static final int MAX_FAILED_ATTEMPTS = 5;
    private static final int LOCK_DURATION_MINUTES = 15;

    /**
     * Authenticate user
     */
    public User authenticate(String username, String password) {
        User user = userDAO.findByUsername(username);
        
        if (user == null) {
            LogUtil.logActivity(null, LogUtil.ActionType.LOGIN, LogUtil.ModuleType.USER,
                    "Login failed - User not found: " + username);
            return null;
        }

        // Check if account is locked
        if (user.getLockedUntil() != null && user.getLockedUntil().isAfter(LocalDateTime.now())) {
            LogUtil.logActivity(user.getUserId(), LogUtil.ActionType.LOGIN, LogUtil.ModuleType.USER,
                    "Login failed - Account locked");
            return null;
        }

        // Verify password
        if (!PasswordUtil.verifyPassword(password, user.getPasswordHash())) {
            // Increment failed attempts
            int newAttempts = user.getFailedLoginAttempts() + 1;
            userDAO.updateFailedLoginAttempts(user.getUserId(), newAttempts);

            if (newAttempts >= MAX_FAILED_ATTEMPTS) {
                LocalDateTime lockedUntil = LocalDateTime.now().plusMinutes(LOCK_DURATION_MINUTES);
                userDAO.lockAccount(user.getUserId(), lockedUntil);
                LogUtil.logActivity(user.getUserId(), LogUtil.ActionType.LOGIN, LogUtil.ModuleType.USER,
                        "Login failed - Account locked after " + MAX_FAILED_ATTEMPTS + " attempts");
            }

            LogUtil.logActivity(user.getUserId(), LogUtil.ActionType.LOGIN, LogUtil.ModuleType.USER,
                    "Login failed - Wrong password (Attempt " + newAttempts + ")");
            return null;
        }

        // Reset failed attempts on successful login
        if (user.getFailedLoginAttempts() > 0) {
            userDAO.updateFailedLoginAttempts(user.getUserId(), 0);
        }

        LogUtil.logActivity(user.getUserId(), LogUtil.ActionType.LOGIN, LogUtil.ModuleType.USER,
                "Login successful");
        return user;
    }

    /**
     * Get user by ID
     */
    public User getUserById(int userId) {
        return userDAO.findById(userId);
    }

    /**
     * Get all users
     */
    public List<User> getAllUsers() {
        return userDAO.findAll();
    }

    /**
     * Create new user
     */
    public boolean createUser(User user) {
        // Hash password
        String hashedPassword = PasswordUtil.hashPassword(user.getPasswordHash());
        user.setPasswordHash(hashedPassword);

        boolean result = userDAO.insert(user);
        if (result) {
            LogUtil.logActivity(null, LogUtil.ActionType.CREATE, LogUtil.ModuleType.USER,
                    "User created: " + user.getUsername());
        }
        return result;
    }

    /**
     * Update user
     */
    public boolean updateUser(User user) {
        boolean result = userDAO.update(user);
        if (result) {
            LogUtil.logActivity(null, LogUtil.ActionType.UPDATE, LogUtil.ModuleType.USER,
                    user.getUserId(), "User updated: " + user.getUsername(), null, null);
        }
        return result;
    }

    /**
     * Delete user
     */
    public boolean deleteUser(int userId) {
        boolean result = userDAO.delete(userId);
        if (result) {
            LogUtil.logActivity(null, LogUtil.ActionType.DELETE, LogUtil.ModuleType.USER,
                    userId, "User deleted", null, null);
        }
        return result;
    }

    /**
     * Search users
     */
    public List<User> searchUsers(String keyword) {
        return userDAO.search(keyword);
    }

    /**
     * Change password
     */
    public boolean changePassword(int userId, String oldPassword, String newPassword) {
        User user = userDAO.findById(userId);
        if (user == null) {
            return false;
        }

        if (!PasswordUtil.verifyPassword(oldPassword, user.getPasswordHash())) {
            return false;
        }

        user.setPasswordHash(PasswordUtil.hashPassword(newPassword));
        boolean result = userDAO.update(user);
        if (result) {
            LogUtil.logActivity(userId, LogUtil.ActionType.UPDATE, LogUtil.ModuleType.USER,
                    "Password changed");
        }
        return result;
    }
}
