package com.example.urbs.service;
import com.example.urbs.data.model.CoordinatorResponse;
import com.example.urbs.data.model.LineResponse;
import com.example.urbs.data.model.LoginResponse;
import com.example.urbs.data.model.ShapeResponse;
import com.example.urbs.data.model.StopResponse;
import com.example.urbs.data.model.User;
import com.example.urbs.data.model.VehicleResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @POST("{path}")
    Call<Void> registerUser(@Path("path") String path, @Body User user);
    @POST("{path}")
    Call<LoginResponse> loginUser(@Path("path") String path, @Query("email") String email, @Query("password") String password);
    @GET("{path}")
    Call<ArrayList<LineResponse>> getLine(@Path("path") String path);
    @GET("{path}")
    Call<ArrayList<ShapeResponse>> getShape(@Path("path") String path);
    @GET("{path}")
    Call<ArrayList<StopResponse>> getStops(@Path("path") String path);
    @GET("{path}")
    Call<ArrayList<VehicleResponse>> getVehicles(@Path("path") String path);
    @POST("{path}")
    Call<Void> saveLocation(@Path("path") String path, @Body CoordinatorResponse coordinator);
}