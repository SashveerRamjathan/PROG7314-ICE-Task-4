package com.st10361554.prog7314_ice_task_4.retrofit

import com.st10361554.prog7314_ice_task_4.models.NewsResponse
import com.st10361554.prog7314_ice_task_4.models.SourcesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit service interface defining all News API endpoints used in the application.
 * This interface uses Retrofit annotations to map Kotlin methods to HTTP API calls.
 * All methods are suspend functions to support coroutines and asynchronous operations.
 *
 * The interface abstracts the HTTP communication details and provides a clean,
 * type-safe way to interact with the News API v2 endpoints. Authentication
 * is handled automatically by the RetrofitUtils interceptor.
 *
 * Official News API Documentation: https://newsapi.org/docs
 */
interface NewsService
{
    /**
     * Fetches top headlines filtered by news category.
     *
     * This method calls the GET /top-headlines endpoint to retrieve the most
     * important and recent news stories from a specific category. The News API
     * returns articles sorted by publication date in descending order.
     *
     * @param category The news category to filter headlines by
     *                 Valid values: "business", "entertainment", "general", "health",
     *                 "science", "sports", "technology"
     *                 Use NewsConstants.Categories for type safety
     * @return Response<NewsResponse> containing status, totalResults, and articles array
     *
     * API Endpoint: GET https://newsapi.org/v2/top-headlines?category={category}
     */
    @GET("top-headlines")
    suspend fun getTopHeadlinesByCategory(
        @Query("category") category: String
    ): Response<NewsResponse>

    /**
     * Retrieves all available news sources from the News API.
     *
     * This method calls the GET /top-headlines/sources endpoint to fetch a complete
     * list of news sources with their metadata including names, descriptions,
     * categories, languages, and countries. Useful for building source selection
     * interfaces and filtering options.
     *
     * @return Response<SourcesResponse> containing status and sources array with full metadata
     *
     * API Endpoint: GET https://newsapi.org/v2/top-headlines/sources
     *
     * Note: This endpoint returns NewsSource objects with complete information,
     * unlike the simplified Source objects embedded within articles.
     */
    @GET("top-headlines/sources")
    suspend fun getAllSources(): Response<SourcesResponse>

    /**
     * Searches for articles across all sources with comprehensive filtering options.
     *
     * This method calls the GET /everything endpoint, which provides the most powerful
     * search capabilities in the News API. It searches through millions of articles
     * from thousands of sources and supports advanced filtering, sorting, and pagination.
     *
     * @param query The search query string (required)
     *              Supports keywords, phrases, and Boolean operators (AND, OR, NOT)
     *              Examples: "Android", "climate change", "Apple AND iPhone", "crypto NOT bitcoin"
     * @param fromDate Optional start date for article filtering (format: YYYY-MM-DD)
     *                 Only articles published on or after this date will be included
     * @param toDate Optional end date for article filtering (format: YYYY-MM-DD)
     *               Only articles published on or before this date will be included
     * @param sortBy Sorting method for results (default: "publishedAt")
     *               Options: "relevancy", "popularity", "publishedAt"
     *               Use SearchConstants.SortBy for type safety
     * @param pageSize Number of articles to return per request (default: 100, max: 100)
     *                 Controls pagination and response size
     * @param page Page number for pagination (default: 1)
     *             Use with pageSize to implement pagination in UI
     * @param language Optional language filter (ISO 639-1 code)
     *                 Examples: "en" (English), "es" (Spanish), "fr" (French)
     * @param searchIn Optional field specification for search scope
     *                 Options: "title", "description", "content"
     *                 Can be comma-separated: "title,description"
     * @param sources Optional comma-separated list of source IDs to search within
     *                Examples: "bbc-news,cnn,reuters"
     *                Cannot be used with domains/excludeDomains
     * @param domains Optional comma-separated list of domains to search within
     *                Examples: "bbc.co.uk,cnn.com"
     *                Cannot be used with sources parameter
     * @param excludeDomains Optional comma-separated list of domains to exclude
     *                       Examples: "example.com,spam-site.com"
     *                       Cannot be used with sources parameter
     * @return Response<NewsResponse> containing filtered, sorted, and paginated articles
     *
     * API Endpoint: GET https://newsapi.org/v2/everything
     */
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