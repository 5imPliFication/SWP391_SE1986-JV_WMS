package com.example.service;

import com.example.dao.UserDAO;
import com.example.model.User;
import com.example.validator.UserValidator;

import java.util.List;

public class UserService {
    private final UserDAO userDAO = new UserDAO();

    public String createUser(User user) {
        // check validate user
        String error = UserValidator.validateCreate(user);

        // if error -> return message error
        if(error != null){
            return error;
        }

        // check exist email
        if(userDAO.login(user.getEmail())!=null){
            return "Email exist, please input other email !!!";
        };

        // insert new user to DB
        boolean statusCreate = userDAO.insertUser(user);
        // return
        return statusCreate ? null : "Can not create new user";
    }

    public List<User> getListUsers() {
        return userDAO.findAll();
    }

    public User findUserById(int id) {
        return userDAO.findUserById(id);
    }

    public boolean changeStatus(int id) {
        // get current status from DB
        boolean currentStatus = userDAO.getCurrentStatus(id);
        // change status
        return userDAO.changeStatus(id, !currentStatus);
    }
}
