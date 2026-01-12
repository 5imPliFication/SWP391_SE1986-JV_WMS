package com.example.validator;

import com.example.model.User;

public class UserValidator {
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

//        if(user.getRole() == null || user.getRole().trim().isEmpty()){
//            return "Role can not be empty";
//        }
        return null;
    }
}
