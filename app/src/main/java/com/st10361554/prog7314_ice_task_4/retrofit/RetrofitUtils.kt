package com.st10361554.prog7314_ice_task_4.retrofit

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import com.st10361554.prog7314_ice_task_4.BuildConfig

/**
 * Utility object for creating and configuring Retrofit instances for News API communication.
 * Implements the singleton pattern to provide a consistent HTTP client configuration
 * across the entire application.
 *
 * This class handles:
 * - API authentication via Bearer token
 * - HTTP timeout configurations
 * - JSON serialization/deserialization
 * - Base URL configuration for News API
 *
 * The configuration is optimized for News API requirements and provides
 * robust error handling through extended timeouts and proper authentication.
 */
object RetrofitUtils
{
    /**
     * The API key for authenticating with the News API.
     * Retrieved from BuildConfig to keep sensitive data out of source code.
     * This token is used in the Authorization header for all API requests.
     */
    private const val BEARER_TOKEN = BuildConfig.NEWS_API_KEY

    /**
     * Creates and configures a Retrofit instance for News API communication.
     *
     * This method sets up a complete HTTP client with authentication, timeouts,
     * and JSON conversion capabilities. The configuration includes:
     * - Bearer token authentication for all requests
     * - Extended timeouts for reliable news data fetching
     * - Gson converter for automatic JSON to Kotlin object mapping
     * - News API v2 base URL
     *
     * @return Configured Retrofit instance ready for creating API service interfaces
     */
    fun retrofit2(): Retrofit
    {
        /**
         * HTTP interceptor that automatically adds Bearer token authentication to all requests.
         *
         * This interceptor intercepts every outgoing request and adds the required
         * Authorization header with the API key. This ensures all API calls are
         * properly authenticated without manually adding headers to each request.
         *
         * The interceptor follows the OAuth 2.0 Bearer Token standard format:
         * Authorization: Bearer {API_KEY}
         */
        val authInterceptor = Interceptor { chain ->
            // Get the original request from the chain
            val originalRequest = chain.request()

            // Create a new request with the Authorization header added
            val requestWithAuth = originalRequest.newBuilder()
                .addHeader("Authorization", "Bearer $BEARER_TOKEN")
                .build()

            // Proceed with the modified request
            chain.proceed(requestWithAuth)
        }

        /**
         * Configure OkHttpClient with custom timeouts and authentication.
         *
         * The timeout values are set to handle potential network delays and
         * server response times when fetching news data. These values provide
         * a good balance between responsiveness and reliability:
         *
         * - Connect timeout: Time to establish connection to server
         * - Read timeout: Time to read response data from server
         * - Write timeout: Time to send request data to server
         */
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS) // Connection timeout - time to establish connection
            .readTimeout(120, TimeUnit.SECONDS)   // Read timeout - time to receive server response
            .writeTimeout(120, TimeUnit.SECONDS)  // Write timeout - time to send request data
            .addInterceptor(authInterceptor)      // Add the authentication interceptor
            .build()

        /**
         * Build and configure the Retrofit instance with all necessary components.
         *
         * This creates the final Retrofit instance that combines:
         * - Base URL for News API v2 endpoints
         * - Configured HTTP client with auth and timeouts
         * - Gson converter for automatic JSON parsing
         *
         * The base URL "https://newsapi.org/v2/" is the official News API v2 endpoint
         * that supports all the news fetching operations used in this application.
         */
        return Retrofit.Builder()
            .baseUrl("https://newsapi.org/v2/")              // Official News API v2 base URL
            .client(okHttpClient)                             // Use our configured HTTP client
            .addConverterFactory(GsonConverterFactory.create()) // Enable JSON to Kotlin object conversion
            .build()
    }
}