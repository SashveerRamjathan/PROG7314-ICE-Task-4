package com.st10361554.prog7314_ice_task_4.retrofit

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitUtils {
    private const val BEARER_TOKEN = "9ab99f1983364d77a00d1883e8fd8027"

    fun retrofit2(): Retrofit
    {
        // Create an interceptor to add the Authorization header
        val authInterceptor = Interceptor { chain ->
            val originalRequest = chain.request()
            val requestWithAuth = originalRequest.newBuilder()
                .addHeader("Authorization", "Bearer $BEARER_TOKEN")
                .build()
            chain.proceed(requestWithAuth)
        }

        // Configure OkHttpClient with extended timeouts and auth interceptor
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS) // Connection timeout
            .readTimeout(120, TimeUnit.SECONDS)   // Read (server response) timeout
            .writeTimeout(120, TimeUnit.SECONDS)  // Write (request sending) timeout
            .addInterceptor(authInterceptor)      // Add the auth interceptor
            .build()

        // Build and return a Retrofit instance with the specified base URL, OkHttpClient, and Gson converter
        return Retrofit.Builder()
            .baseUrl("https://newsapi.org/v2/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}