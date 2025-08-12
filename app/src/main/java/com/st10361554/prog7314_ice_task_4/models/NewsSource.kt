package com.st10361554.prog7314_ice_task_4.models

import com.google.gson.annotations.SerializedName

/**
 * Data class representing a complete news source with all available metadata.
 * This model is used for the News API sources endpoint which provides detailed information
 * about available news sources including their categories, languages, and countries.
 * Uses Gson annotations for proper JSON to Kotlin object mapping.
 *
 * This is the full source model (unlike the simplified Source class used within articles)
 * and contains comprehensive details for displaying source listings and filtering.
 */
data class NewsSource(
    /**
     * The unique identifier for the news source.
     * Used as a key for API requests and internal referencing.
     */
    @SerializedName("id")
    val id: String,

    /**
     * The human-readable display name of the news source.
     * This is what users see in the UI when browsing sources.
     */
    @SerializedName("name")
    val name: String,

    /**
     * A brief description of the news source and its content focus.
     * Provides context about what type of news and coverage the source offers.
     * Used to help users understand the source's purpose and scope.
     */
    @SerializedName("description")
    val description: String,

    /**
     * The official website URL of the news source.
     * Used for click-through functionality to visit the source's homepage.
     * Always a complete, valid URL that can be opened in a browser.
     */
    @SerializedName("url")
    val url: String,

    /**
     * The primary category or topic focus of the news source.
     * Used for filtering and organizing sources by content type.
     */
    @SerializedName("category")
    val category: String,

    /**
     * The primary language of the news source content.
     * Represented as a two-letter ISO 639-1 language code.
     */
    @SerializedName("language")
    val language: String,

    /**
     * The country where the news source is based or primarily operates.
     * Represented as a two-letter ISO 3166-1 country code.
     */
    @SerializedName("country")
    val country: String
)