package com.example.service;

import com.example.dao.PasswordResetDAO;
import com.example.model.PasswordReset;
import com.example.util.PasswordUtil;

import java.time.LocalDateTime;
import java.util.List;

public class PasswordResetService {
    private PasswordResetDAO passwordResetDAO = new PasswordResetDAO();

    public boolean createPasswordResetRequest(Long userId) {
        String status = "PENDING";
        return passwordResetDAO.insert(userId, status);
    }

    public List<PasswordReset> findAll(String searchName, String requestStatus) {
        return passwordResetDAO.getAll(searchName, requestStatus);
    }

    public boolean updateStatus(Long passwordResetId, String status) {
        return passwordResetDAO.updateStatusById(passwordResetId, status);
    }
}
