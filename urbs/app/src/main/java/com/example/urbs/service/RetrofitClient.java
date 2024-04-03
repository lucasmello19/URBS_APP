package com.example.urbs.service;

import android.content.Context;

import com.example.urbs.utils.AccessTokenManager;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit;
    private static final String BASE_URL = "https://backend.tccurbstads.com/";

    public static Retrofit getClient(Context context) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        String accessToken = AccessTokenManager.getInstance(context).getAccessToken();

        if (accessToken != null) {
            // Adiciona o interceptor de autenticação apenas se o accessToken não for nulo
            httpClient.addInterceptor(new AuthInterceptor(accessToken));
        }
        httpClient.addInterceptor(new LoggingInterceptor()); // Adiciona o interceptor de log

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build()) // Configura o cliente OkHttp com o interceptor
                .build();

        return retrofit;
    }
}

