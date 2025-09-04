package com.example.tp.data.model;

public class LoginRequest {
    private String email;
    private String password;

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getters (optional, if needed later)
    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
