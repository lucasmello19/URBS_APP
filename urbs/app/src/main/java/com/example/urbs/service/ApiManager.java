package com.example.urbs.service;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.shapes.Shape;
import android.preference.PreferenceManager;

import com.example.urbs.data.model.LineResponse;
import com.example.urbs.data.model.LoginResponse;
import com.example.urbs.data.model.ShapeResponse;
import com.example.urbs.data.model.StopResponse;
import com.example.urbs.data.model.User;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import com.example.urbs.utils.AccessTokenManager;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ApiManager {

    private ApiService apiService;

    public ApiManager(Context context) {
        Retrofit retrofit = RetrofitClient.getClient(context);
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
                            AccessTokenManager.getInstance(context).setAccessToken(accessToken);
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

    public void getLine(final ApiCallback<ArrayList<LineResponse>> callback) {
        Call<ArrayList<LineResponse>> call = apiService.getLine("lines");
        call.enqueue(new Callback<ArrayList<LineResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<LineResponse>> call, Response<ArrayList<LineResponse>> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body()); // Passa a lista de linhas para o método onSuccess
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
            public void onFailure(Call<ArrayList<LineResponse>> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }

    public void getShape(String cod, final ApiCallback<ArrayList<ShapeResponse>> callback) {
        Call<ArrayList<ShapeResponse>> call = apiService.getShape("shape/" + cod);
        call.enqueue(new Callback<ArrayList<ShapeResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<ShapeResponse>> call, Response<ArrayList<ShapeResponse>> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body()); // Passa a lista de linhas para o método onSuccess
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
            public void onFailure(Call<ArrayList<ShapeResponse>> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }

    public void getStops(String cod, final ApiCallback<ArrayList<StopResponse>> callback) {
        Call<ArrayList<StopResponse>> call = apiService.getStops("stops/" + cod);
        call.enqueue(new Callback<ArrayList<StopResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<StopResponse>> call, Response<ArrayList<StopResponse>> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body()); // Passa a lista de linhas para o método onSuccess
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
            public void onFailure(Call<ArrayList<StopResponse>> call, Throwable t) {
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
