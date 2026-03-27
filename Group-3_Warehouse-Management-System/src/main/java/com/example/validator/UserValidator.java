package com.example.validator;

import com.example.model.User;

import java.util.regex.Pattern;

public class UserValidator {

    private static final String PASSWORD_REGEX = "^(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}$";
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);
    private static final int MAX_PASSWORD_LENGTH = 128;

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

    public static boolean validatePassword(String password){
        if(password == null || password.trim().isEmpty()){
            return false;
        }

        if (password.length() > MAX_PASSWORD_LENGTH) {
            return false;
        }
        return PASSWORD_PATTERN.matcher(password).matches();
    }
}
