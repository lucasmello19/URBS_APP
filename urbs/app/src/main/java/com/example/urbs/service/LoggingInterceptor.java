package com.example.urbs.service;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

import java.io.IOException;

public class LoggingInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        long startTime = System.nanoTime();
        System.out.println("Sending request: " + request.url());
        System.out.println("Sending request: " + request.body());


        Response response = chain.proceed(request);

        long endTime = System.nanoTime();
        System.out.println("Received response for " + response.request().url() + " in " + ((endTime - startTime) / 1e6) + "ms");

        // Log do body da resposta (opcional)
        ResponseBody responseBody = response.body();
        if (responseBody != null) {
            String responseBodyString = responseBody.string();
            System.out.println("Response body: " + responseBodyString);

            // Recria o body da resposta, pois o original já foi consumido
            response = response.newBuilder()
                    .body(ResponseBody.create(responseBody.contentType(), responseBodyString.getBytes()))
                    .build();
        }

        return response;
    }
}
