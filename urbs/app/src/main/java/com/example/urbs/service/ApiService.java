package com.example.urbs.service;
import com.example.urbs.data.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    @POST("{path}")
    Call<Void> registerUser(@Path("path") String path, @Body User user);
}