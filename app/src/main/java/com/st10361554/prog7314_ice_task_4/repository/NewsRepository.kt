package com.st10361554.prog7314_ice_task_4.repository

import com.st10361554.prog7314_ice_task_4.models.NewsResponse
import com.st10361554.prog7314_ice_task_4.models.SourcesResponse
import com.st10361554.prog7314_ice_task_4.retrofit.RetrofitUtils
import com.st10361554.prog7314_ice_task_4.retrofit.NewsService
import retrofit2.Response

/**
 * Repository class that serves as the single source of truth for news data.
 * Implements the Repository pattern to abstract the data layer from the UI/ViewModel layers.
 *
 * This class handles all network communication with the News API through Retrofit,
 * providing a clean interface for fetching news articles, headlines, and sources.
 * All methods are suspend functions to support coroutines and asynchronous operations.
 *
 * The repository encapsulates the data fetching logic and can be easily extended
 * to include caching, offline support, or multiple data sources in the future.
 */
class NewsRepository {
    /**
     * Retrofit service instance for making News API calls.
     * Created using the RetrofitUtils factory to ensure consistent configuration
     * including base URL, converters, and HTTP client settings.
     */
    private val newsService = RetrofitUtils.retrofit2().create(NewsService::class.java)

    /**
     * Fetches top headlines filtered by a specific news category.
     *
     * This method calls the News API's /top-headlines endpoint with category filtering
     * to retrieve the most important and recent news stories from a particular subject area.
     *
     * @param category The news category to filter by (e.g., "business", "technology", "sports")
     *                 Should use constants from NewsConstants.Categories for consistency
     * @return Response<NewsResponse> containing the API response with status, totalResults, and articles
     */
    suspend fun getTopHeadlinesByCategory(
        category: String
    ): Response<NewsResponse> {
        return newsService.getTopHeadlinesByCategory(
            category = category
        )
    }

    /**
     * Retrieves all available news sources from the News API.
     *
     * This method calls the /sources endpoint to fetch a comprehensive list
     * of news sources with their metadata including categories, languages, and countries.
     * Useful for displaying source selection interfaces or filtering options.
     *
     * @return Response<SourcesResponse> containing the API response with status and sources list
     */
    suspend fun getAllSources(): Response<SourcesResponse> {
        return newsService.getAllSources()
    }

    /**
     * Searches for news articles across all sources with advanced filtering options.
     *
     * This method calls the News API's /everything endpoint which provides the most
     * comprehensive search capabilities including keyword search, date range filtering,
     * and result sorting. Ideal for implementing search functionality and custom news feeds.
     *
     * @param query The search query string (keywords, phrases, or advanced search operators)
     *              Example: "Android development", "COVID-19", "Apple OR Google"
     * @param fromDate Optional start date for filtering articles (format: "YYYY-MM-DD")
     *                 Only articles published after this date will be included
     * @param toDate Optional end date for filtering articles (format: "YYYY-MM-DD")
     *               Only articles published before this date will be included
     * @param sortBy Sorting method for results (default: "publishedAt")
     *               Should use constants from SearchConstants.SortBy for consistency
     *               Options: "relevancy", "popularity", "publishedAt"
     * @return Response<NewsResponse> containing the API response with filtered and sorted articles
     */
    suspend fun searchEverything(
        query: String,
        fromDate: String? = null,
        toDate: String? = null,
        sortBy: String = "publishedAt"
    ): Response<NewsResponse> {
        return newsService.searchEverything(
            query = query,
            fromDate = fromDate,
            toDate = toDate,
            sortBy = sortBy
        )
    }
}