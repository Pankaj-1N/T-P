package com.example.tp.data.api;

import com.example.tp.data.model.LoginRequest;
import com.example.tp.data.model.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("api/login")
    Call<LoginResponse> login(@Body LoginRequest request);
}
