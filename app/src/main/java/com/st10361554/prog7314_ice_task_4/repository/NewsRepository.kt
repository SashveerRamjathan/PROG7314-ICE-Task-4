package com.st10361554.prog7314_ice_task_4.repository

import com.st10361554.prog7314_ice_task_4.models.NewsResponse
import com.st10361554.prog7314_ice_task_4.models.SourcesResponse
import com.st10361554.prog7314_ice_task_4.retrofit.RetrofitUtils
import com.st10361554.prog7314_ice_task_4.retrofit.NewsService
import retrofit2.Response

class NewsRepository {
    private val newsService = RetrofitUtils.retrofit2().create(NewsService::class.java)

    suspend fun getTopHeadlinesByCategory(
        category: String
    ): Response<NewsResponse> {
        return newsService.getTopHeadlinesByCategory(
            category = category
        )
    }

    // Get all available sources
    suspend fun getAllSources(): Response<SourcesResponse> {
        return newsService.getAllSources()
    }
}