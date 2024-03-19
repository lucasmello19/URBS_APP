package com.example.urbs.service;
import com.example.urbs.data.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ApiManager {

    private ApiService apiService;

    public ApiManager() {
        Retrofit retrofit = RetrofitClient.getClient();
        apiService = retrofit.create(ApiService.class);
    }

    public void registerUser(User user, final ApiCallback<Void> callback) {
        Call<Void> call = apiService.registerUser("register", user);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(null);
                } else {
                    callback.onFailure(new Exception("Registration failed"));
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }

    // Interface para callback
    public interface ApiCallback<T> {
        void onSuccess(T result);
        void onFailure(Throwable t);
    }
}
