package com.st10361554.prog7314_ice_task_4.models

import com.google.gson.annotations.SerializedName

/**
 * Data class representing a news article from the News API.
 * This model contains all the essential information for a news article including
 * metadata, content, and media references. Used primarily for displaying articles
 * in lists and detail views within the application.
 * Uses Gson annotations for proper JSON to Kotlin object mapping.
 */
data class Article(
    /**
     * The news source that published this article.
     * Contains source identification and display name information.
     * Used for attribution and source filtering in the UI.
     */
    @SerializedName("source")
    val source: Source,

    /**
     * The author(s) of the news article.
     * Can be null if the author information is not available from the source.
     * Used for displaying byline information and journalist attribution.
     */
    @SerializedName("author")
    val author: String?,

    /**
     * The headline or title of the news article.
     * Always present and serves as the primary identifier for the article.
     * Used as the main display text in article lists and detail headers.
     */
    @SerializedName("title")
    val title: String,

    /**
     * A brief summary or excerpt of the article content.
     * Can be null if no description is provided by the source.
     * Used for preview text in article lists and search results.
     */
    @SerializedName("description")
    val description: String?,

    /**
     * The direct URL link to the full article on the source's website.
     * Always present and used for click-through functionality to read the complete article.
     * Opens in browser when users want to read the full story.
     */
    @SerializedName("url")
    val url: String,

    /**
     * URL of the main image associated with the article.
     * Can be null if no image is available for the article.
     * Used for displaying article thumbnails and featured images in the UI.
     */
    @SerializedName("urlToImage")
    val urlToImage: String?,

    /**
     * The publication timestamp of the article in ISO 8601 format.
     * Always present as a string in the format "yyyy-MM-dd'T'HH:mm:ss'Z'".
     * Used for sorting articles chronologically and displaying publication dates.
     * Requires formatting for user-friendly display in the UI.
     */
    @SerializedName("publishedAt")
    val publishedAt: String,

    /**
     * A truncated version of the article content (usually first 200 characters).
     * Can be null if content is not available from the source.
     * Note: This is not the full article text - users need to visit the URL for complete content.
     * Used for providing additional preview text beyond the description.
     */
    @SerializedName("content")
    val content: String?
)