package com.st10361554.prog7314_ice_task_4.retrofit

import com.st10361554.prog7314_ice_task_4.models.NewsResponse
import com.st10361554.prog7314_ice_task_4.models.SourcesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsService
{
    // Get top headlines by category
    @GET("top-headlines")
    suspend fun getTopHeadlinesByCategory(
        @Query("category") category: String
    ): Response<NewsResponse>

    // SOURCES ENDPOINT
    @GET("top-headlines/sources")
    suspend fun getSources(
        @Query("category") category: String? = null,
        @Query("language") language: String? = null,
        @Query("country") country: String? = null
    ): Response<SourcesResponse>

    // Get all available sources
    @GET("top-headlines/sources")
    suspend fun getAllSources(): Response<SourcesResponse>

    // Get sources by category
    @GET("top-headlines/sources")
    suspend fun getSourcesByCategory(
        @Query("category") category: String
    ): Response<SourcesResponse>

    // Get sources by country
    @GET("top-headlines/sources")
    suspend fun getSourcesByCountry(
        @Query("country") country: String
    ): Response<SourcesResponse>

    // Get sources by language
    @GET("top-headlines/sources")
    suspend fun getSourcesByLanguage(
        @Query("language") language: String
    ): Response<SourcesResponse>

    // Get sources with multiple filters
    @GET("top-headlines/sources")
    suspend fun getSourcesFiltered(
        @Query("category") category: String? = null,
        @Query("language") language: String? = null,
        @Query("country") country: String? = null
    ): Response<SourcesResponse>
}