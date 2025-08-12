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

    // Get all available sources
    @GET("top-headlines/sources")
    suspend fun getAllSources(): Response<SourcesResponse>

    // Search everything endpoint
    @GET("everything")
    suspend fun searchEverything(
        @Query("q") query: String,
        @Query("from") fromDate: String? = null,
        @Query("to") toDate: String? = null,
        @Query("sortBy") sortBy: String = "publishedAt",
        @Query("pageSize") pageSize: Int = 100,
        @Query("page") page: Int = 1,
        @Query("language") language: String? = null,
        @Query("searchIn") searchIn: String? = null,
        @Query("sources") sources: String? = null,
        @Query("domains") domains: String? = null,
        @Query("excludeDomains") excludeDomains: String? = null
    ): Response<NewsResponse>
}