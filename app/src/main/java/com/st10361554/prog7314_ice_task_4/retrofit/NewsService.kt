package com.st10361554.prog7314_ice_task_4.retrofit

import com.st10361554.prog7314_ice_task_4.models.NewsResponse
import com.st10361554.prog7314_ice_task_4.models.SourcesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsService {

    // EVERYTHING ENDPOINT
    @GET("everything")
    suspend fun getEverything(
        @Query("q") query: String? = null,
        @Query("searchIn") searchIn: String? = null,
        @Query("sources") sources: String? = null,
        @Query("domains") domains: String? = null,
        @Query("excludeDomains") excludeDomains: String? = null,
        @Query("from") from: String? = null,
        @Query("to") to: String? = null,
        @Query("language") language: String? = null,
        @Query("sortBy") sortBy: String? = null,
        @Query("pageSize") pageSize: Int? = null,
        @Query("page") page: Int? = null
    ): Response<NewsResponse>

    // Simplified version with just the most common parameters
    @GET("everything")
    suspend fun searchArticles(
        @Query("q") query: String,
        @Query("sortBy") sortBy: String = "publishedAt",
        @Query("pageSize") pageSize: Int = 20,
        @Query("page") page: Int = 1
    ): Response<NewsResponse>

    // Version for getting articles from specific domains
    @GET("everything")
    suspend fun getArticlesByDomain(
        @Query("domains") domains: String,
        @Query("from") from: String? = null,
        @Query("to") to: String? = null,
        @Query("sortBy") sortBy: String = "publishedAt",
        @Query("pageSize") pageSize: Int = 20
    ): Response<NewsResponse>

    // TOP HEADLINES ENDPOINT (existing)
    @GET("top-headlines")
    suspend fun getTopHeadlines(
        @Query("country") country: String? = null,
        @Query("category") category: String? = null,
        @Query("sources") sources: String? = null,
        @Query("q") query: String? = null,
        @Query("pageSize") pageSize: Int = 20,
        @Query("page") page: Int = 1
    ): Response<NewsResponse>

    // Get top headlines by country
    @GET("top-headlines")
    suspend fun getTopHeadlinesByCountry(
        @Query("country") country: String,
        @Query("pageSize") pageSize: Int = 20,
        @Query("page") page: Int = 1
    ): Response<NewsResponse>

    // Get top headlines by category
    @GET("top-headlines")
    suspend fun getTopHeadlinesByCategory(
        @Query("category") category: String,
        @Query("country") country: String? = null,
        @Query("pageSize") pageSize: Int = 20,
        @Query("page") page: Int = 1
    ): Response<NewsResponse>

    // Get top headlines by sources
    @GET("top-headlines")
    suspend fun getTopHeadlinesBySources(
        @Query("sources") sources: String,
        @Query("pageSize") pageSize: Int = 20,
        @Query("page") page: Int = 1
    ): Response<NewsResponse>

    // Search top headlines with query
    @GET("top-headlines")
    suspend fun searchTopHeadlines(
        @Query("q") query: String,
        @Query("country") country: String? = null,
        @Query("category") category: String? = null,
        @Query("pageSize") pageSize: Int = 20,
        @Query("page") page: Int = 1
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