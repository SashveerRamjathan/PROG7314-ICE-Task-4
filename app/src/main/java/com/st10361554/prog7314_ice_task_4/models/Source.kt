package com.st10361554.prog7314_ice_task_4.models

import com.google.gson.annotations.SerializedName

/**
 * Data class representing a news source within an article response.
 * This is a simplified version of news source information that is embedded within Article objects.
 * Uses Gson annotations for proper JSON to Kotlin object mapping.
 *
 * Note: This differs from NewsSource which contains full source details for the sources endpoint.
 * This Source class is specifically for the source information nested within news articles.
 */
data class Source(
    /**
     * The unique identifier for the news source.
     * Can be null for some sources that don't have a specific ID in the News API.
     * Used to identify and categorize the source of the news article.
     */
    @SerializedName("id")
    val id: String?,

    /**
     * The display name of the news source.
     * This is the human-readable name shown to users (e.g., "BBC News", "CNN", "Reuters").
     * Always present and used for displaying the source information in the UI.
     */
    @SerializedName("name")
    val name: String

)