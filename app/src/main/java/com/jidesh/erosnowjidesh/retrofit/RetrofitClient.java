package com.jidesh.erosnowjidesh.retrofit;

import android.content.Context;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit;
    public static final String BASE_URL = "https://api.themoviedb.org/3/";
    public static Retrofit getRetrofitInstance(Context context) {
        if (retrofit == null) {
            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
                @NotNull
                @Override
                public Response intercept(@NotNull Chain chain) throws IOException {
                    Request request = chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer " + "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI5YjA2NDc5NDE1YzYxYWUyYTNhZTc1NWM1M2YxNWY2YiIsInN1YiI6IjVlN2RhZTdlMjc4ZDhhMDAxMTcyZDUxOCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.kgHVG6QOLO-9Cuu4JYZbdWw-dXVwU_foJAFomVo61v4")
                            .addHeader("Content-Type", "application/json").build();
                    return chain.proceed(request);
                }
            }).addInterceptor(httpLoggingInterceptor).build();

            retrofit = new Retrofit.Builder().client(client).addConverterFactory(GsonConverterFactory.create()).baseUrl(BASE_URL).build();
            return retrofit;
        }
        return retrofit;
    }

}
