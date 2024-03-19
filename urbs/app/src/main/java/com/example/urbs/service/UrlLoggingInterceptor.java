package com.example.urbs.service;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class UrlLoggingInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        HttpUrl url = request.url();
        System.out.println("URL chamada: " + url.toString());

        // Continue a execução da solicitação
        return chain.proceed(request);
    }
}
