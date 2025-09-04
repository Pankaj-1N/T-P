package com.example.tp.data.model;

public class LoginResponse {
    private String token;  // present if login is successful
    private String error;  // present if login fails

    public String getToken() {
        return token;
    }

    public String getError() {
        return error;
    }
}
