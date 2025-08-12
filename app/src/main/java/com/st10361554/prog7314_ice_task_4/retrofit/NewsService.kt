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
}