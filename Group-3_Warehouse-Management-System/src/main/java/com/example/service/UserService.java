package com.example.service;

import com.example.dao.UserDAO;
import com.example.model.User;
import com.example.validator.UserValidator;

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
        if(userDAO.isExistEmail(user.getEmail())){
            return "Email exist, please input other email !!!";
        };

        // insert new user to DB
        boolean statusCreate = userDAO.insertUser(user);
        // return
        return statusCreate ? null : "Can not create new user";
    }
}
