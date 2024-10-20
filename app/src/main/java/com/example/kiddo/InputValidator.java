package com.example.kiddo;

import android.util.Patterns;
import android.widget.EditText;

public class InputValidator {
    public boolean validateUsername(EditText usernameEditText) {
        String username = usernameEditText.getText().toString().trim();
        if (username.isEmpty() || username.length() < 3) {
            usernameEditText.setError("Username must be at least 3 characters long");
            return false;
        }
        return true;
    }

    public boolean validateEmail(EditText emailEditText) {
        String email = emailEditText.getText().toString().trim();
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Invalid email address");
            return false;
        }
        return true;
    }

    public boolean validatePassword(EditText passwordEditText) {
        String password = passwordEditText.getText().toString().trim();
        if (password.isEmpty() || password.length() < 6) {
            passwordEditText.setError("Password must be at least 6 characters long");
            return false;
        }
        return true;
    }

    public boolean validateConfirmPassword(EditText passwordEditText, EditText confirmPasswordEditText) {
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();
        if (!confirmPassword.equals(password)) {
            confirmPasswordEditText.setError("Passwords do not match");
            return false;
        }
        return true;
    }
}