package com.example.urbs.service;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {

    private String accessToken;

    public AuthInterceptor(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();

        // Adiciona o token de acesso como um cabeçalho na solicitação original
        Request modifiedRequest = originalRequest.newBuilder()
                .header("Authorization", "Bearer " + accessToken)
                .build();

        return chain.proceed(modifiedRequest);
    }
}
