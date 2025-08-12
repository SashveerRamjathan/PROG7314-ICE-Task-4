package com.st10361554.prog7314_ice_task_4.news_utils

/**
 * Object containing constant values for News API category filtering and operations.
 * Centralizes all news-related constants to ensure consistency across the application
 * and provides a single source of truth for News API parameter values.
 *
 * This object helps maintain type safety and prevents typos when specifying
 * category parameters in API requests, especially for top headlines filtering.
 */
object NewsConstants
{
    object Categories {
        /**
         * Business and financial news category.
         * Includes corporate news, market updates, economic reports,
         * company earnings, and financial analysis articles.
         */
        const val BUSINESS = "business"

        /**
         * Entertainment and celebrity news category.
         * Covers movies, television, music, celebrity gossip,
         * awards shows, and general entertainment industry news.
         */
        const val ENTERTAINMENT = "entertainment"

        /**
         * General news category covering broad topics.
         * This is often the default category that includes various news types
         * that don't fit into other specific categories, such as politics,
         * current events, and miscellaneous news stories.
         */
        const val GENERAL = "general"

        /**
         * Health and medical news category.
         * Includes medical research, healthcare policy, disease outbreaks,
         * wellness tips, pharmaceutical news, and public health updates.
         */
        const val HEALTH = "health"

        /**
         * Science and research news category.
         * Covers scientific discoveries, research studies, space exploration,
         * environmental science, and technological innovations in research.
         */
        const val SCIENCE = "science"

        /**
         * Sports and athletics news category.
         * Includes game results, player transfers, tournaments,
         * league updates, and general sports-related news across all sports.
         */
        const val SPORTS = "sports"

        /**
         * Technology and innovation news category.
         * Covers tech company news, product launches, software updates,
         * cybersecurity, artificial intelligence, and digital innovation stories.
         */
        const val TECHNOLOGY = "technology"
    }
}