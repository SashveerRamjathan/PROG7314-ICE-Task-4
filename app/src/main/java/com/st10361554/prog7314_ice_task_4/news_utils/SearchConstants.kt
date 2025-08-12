package com.st10361554.prog7314_ice_task_4.news_utils

/**
 * Object containing constant values used for News API search operations.
 * Centralizes all search-related constants to ensure consistency across the application
 * and avoid hardcoded strings scattered throughout the codebase.
 *
 * This follows the single source of truth principle for API parameter values
 * and makes it easier to maintain and update search functionality.
 */
object SearchConstants
{
    object SortBy {
        /**
         * Sort articles by relevance to the search query.
         * This is typically the default sorting method that ranks articles
         * based on how well they match the search terms and query parameters.
         * Best used when searching for specific topics or keywords.
         */
        const val RELEVANCY = "relevancy"

        /**
         * Sort articles by popularity and social engagement.
         * This sorting method prioritizes articles that have received
         * more shares, clicks, and general user engagement.
         * Best used for discovering trending and widely-discussed stories.
         */
        const val POPULARITY = "popularity"

        /**
         * Sort articles by publication date in descending order (newest first).
         * This sorting method shows the most recently published articles at the top.
         * Best used for getting the latest news and breaking stories.
         */
        const val PUBLISHED_AT = "publishedAt"
    }
}