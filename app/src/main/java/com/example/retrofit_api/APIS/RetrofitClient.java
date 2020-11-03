package com.example.retrofit_api.APIS;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit =null;

    private static Interceptor interceptor = chain ->{
        Request request = chain.request();
        Request.Builder builder = request.newBuilder().addHeader("Cache-Control", "no-cache");
        request = builder.build();
        return chain.proceed(request);
    };

    private static OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .readTimeout(60, TimeUnit.SECONDS)
            .cache(null)
            .build();

    public static Retrofit getClient(String base_url) {
        retrofit = new Retrofit.Builder()
                .baseUrl(base_url)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }
}

