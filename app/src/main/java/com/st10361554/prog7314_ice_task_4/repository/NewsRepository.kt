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

    // SOURCES ENDPOINT METHODS

    // Get all available sources
    suspend fun getAllSources(): Response<SourcesResponse> {
        return newsService.getAllSources()
    }

    // Get sources by category
    suspend fun getSourcesByCategory(category: String): Response<SourcesResponse> {
        return newsService.getSourcesByCategory(category)
    }

    // Get sources by country
    suspend fun getSourcesByCountry(country: String): Response<SourcesResponse> {
        return newsService.getSourcesByCountry(country)
    }

    // Get sources by language
    suspend fun getSourcesByLanguage(language: String): Response<SourcesResponse> {
        return newsService.getSourcesByLanguage(language)
    }

    // Get sources with multiple filters
    suspend fun getSourcesFiltered(
        category: String? = null,
        language: String? = null,
        country: String? = null
    ): Response<SourcesResponse> {
        return newsService.getSourcesFiltered(
            category = category,
            language = language,
            country = country
        )
    }

    // Get business sources only
    suspend fun getBusinessSources(): Response<SourcesResponse> {
        return newsService.getSourcesByCategory("business")
    }

    // Get US sources only
    suspend fun getUSSources(): Response<SourcesResponse> {
        return newsService.getSourcesByCountry("us")
    }

    // Get English language sources
    suspend fun getEnglishSources(): Response<SourcesResponse> {
        return newsService.getSourcesByLanguage("en")
    }
}