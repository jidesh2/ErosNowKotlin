package com.jidesh.erosnowjidesh.retrofit

import android.content.Context
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private var retrofit: Retrofit? = null
    const val BASE_URL = "https://api.themoviedb.org/3/"
    @JvmStatic
    fun getRetrofitInstance(context: Context?): Retrofit? {
        if (retrofit == null) {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder().addInterceptor(object : Interceptor {
                @Throws(IOException::class)
                override fun intercept(chain: Interceptor.Chain): Response {
                    val request = chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer " + "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI5YjA2NDc5NDE1YzYxYWUyYTNhZTc1NWM1M2YxNWY2YiIsInN1YiI6IjVlN2RhZTdlMjc4ZDhhMDAxMTcyZDUxOCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.kgHVG6QOLO-9Cuu4JYZbdWw-dXVwU_foJAFomVo61v4")
                            .addHeader("Content-Type", "application/json").build()
                    return chain.proceed(request)
                }
            }).addInterceptor(httpLoggingInterceptor).build()
            retrofit = Retrofit.Builder().client(client).addConverterFactory(GsonConverterFactory.create()).baseUrl(BASE_URL).build()
            return retrofit
        }
        return retrofit
    }
}