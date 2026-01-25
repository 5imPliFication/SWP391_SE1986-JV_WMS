package com.example.service;

import com.example.dao.PasswordResetDAO;
import com.example.model.PasswordReset;

import java.util.List;

public class PasswordResetService {
    private PasswordResetDAO passwordResetDAO = new PasswordResetDAO();

    public boolean createPasswordResetRequest(Long userId) {
        String status = "PENDING";
        return passwordResetDAO.insert(userId, status);
    }

    public List<PasswordReset> findAll(String searchName, String requestStatus, int pageNo) {
        return passwordResetDAO.getAll(searchName, requestStatus, pageNo);
    }

    public boolean updateStatus(Long passwordResetId, String status) {
        return passwordResetDAO.updateStatusById(passwordResetId, status);
    }

    public int getTotalRequest(String searchName, String requestStatus) {
        return passwordResetDAO.countRequests(searchName, requestStatus);
    }
}
