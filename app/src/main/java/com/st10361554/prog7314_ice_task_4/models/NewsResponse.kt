package com.st10361554.prog7314_ice_task_4.models

import com.google.gson.annotations.SerializedName

/**
 * Data class representing the API response for news articles endpoint.
 * This class is used to deserialize JSON responses from the News API when fetching news articles
 * from endpoints like /everything, /top-headlines, etc.
 * Uses Gson annotations for proper JSON to Kotlin object mapping.
 */
data class NewsResponse(
    /**
     * The status of the API response.
     * Indicates whether the request was successful or encountered an error.
     * Used for error handling and response validation.
     */
    @SerializedName("status")
    val status: String,

    /**
     * The total number of articles available for the given query.
     * This represents the complete count of matching articles, not just those returned in this response.
     * Useful for pagination calculations and displaying result counts to users.
     */
    @SerializedName("totalResults")
    val totalResults: Int,

    /**
     * List of news articles returned by the API.
     * Contains Article objects with complete news data including title, description, content, etc.
     * This is the main data payload containing the actual news articles to be displayed.
     */
    @SerializedName("articles")
    val articles: List<Article>
)