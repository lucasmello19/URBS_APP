package com.example.urbs.service;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.urbs.data.model.LoginResponse;
import com.example.urbs.data.model.User;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

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
                    String errorMessage = null;
                    try {
                        errorMessage = parseErrorMessage(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    callback.onFailure(new Exception(errorMessage != null ? errorMessage : "Registration failed"));
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }

    public void loginUser(final Context context, String email, String password, final ApiCallback<Void> callback) {
        Call<LoginResponse> call = apiService.loginUser("login", email, password);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    // Verifica se o corpo da resposta não é nulo
                    LoginResponse loginResponse = response.body();
                    if (loginResponse != null) {
                        String accessToken = loginResponse.getAccess_token();
                        if (accessToken != null && !accessToken.isEmpty()) {
                            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("access_token", accessToken);
                            editor.apply();

                            // Imprime o access token
                            System.out.println("Access Token: " + accessToken);
                        }
                    }

                    callback.onSuccess(null);
                } else {
                    String errorMessage = null;
                    try {
                        errorMessage = parseErrorMessage(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    callback.onFailure(new Exception(errorMessage != null ? errorMessage : "login failed"));
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }


    // Interface para callback
    public interface ApiCallback<T> {
        void onSuccess(T result);
        void onFailure(Throwable t);
    }

    public String parseErrorMessage(String responseBody) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(responseBody);
            String detail = rootNode.get("detail").asText();

            return detail;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
