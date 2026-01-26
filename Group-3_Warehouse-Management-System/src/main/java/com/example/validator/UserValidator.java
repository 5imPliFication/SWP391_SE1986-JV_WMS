package com.example.validator;

import com.example.model.User;

public class UserValidator {

    // check input create new user
    public static String validateCreate(User user){
        if (user.getFullName() == null || user.getFullName().trim().isEmpty()) {
            return "Fullname can not be empty";
        }

        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            return "Email can not be empty";
        }

        if (user.getPasswordHash() == null || user.getPasswordHash().trim().isEmpty()) {
            return "Password can not be empty";
        }

        // if role == null or role is default value
        if(user.getRole() == null || user.getRole().getId() == 0){
            return "Role can not be empty";
        }
        return null;
    }
}
