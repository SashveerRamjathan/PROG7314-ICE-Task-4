package com.st10361554.prog7314_ice_task_4.models

import com.google.gson.annotations.SerializedName

/**
 * Data class representing the API response for news sources endpoint.
 * This class is used to deserialize JSON responses from the News API when fetching available news sources.
 * Uses Gson annotations for proper JSON to Kotlin object mapping.
 */
data class SourcesResponse(
    /**
     * The status of the API response.
     * This field helps determine if the API call was successful.
     */
    @SerializedName("status")
    val status: String,

    /**
     * List of available news sources returned by the API.
     * Contains NewsSource objects with details like name, description, category, etc.
     * This is the main data payload containing all available news sources.
     */
    @SerializedName("sources")
    val sources: List<NewsSource>
)