package com.example.tp.data.repository;

import com.example.tp.data.api.ApiService;
import com.example.tp.data.api.RetrofitClient;
import com.example.tp.data.model.LoginRequest;
import com.example.tp.data.model.LoginResponse;

import retrofit2.Call;

public class AuthRepository {
    private ApiService apiService;

    public AuthRepository() {
        apiService = RetrofitClient.getClient().create(ApiService.class);
    }

    // Function to call the login API
    public Call<LoginResponse> login(String email, String password) {
        return apiService.login(new LoginRequest(email, password));
    }
}
