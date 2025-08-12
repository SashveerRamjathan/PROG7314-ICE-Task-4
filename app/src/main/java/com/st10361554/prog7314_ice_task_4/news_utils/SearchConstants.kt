package com.st10361554.prog7314_ice_task_4.news_utils

object SearchConstants
{
    object SortBy {
        const val RELEVANCY = "relevancy"
        const val POPULARITY = "popularity"
        const val PUBLISHED_AT = "publishedAt"
    }

    object SearchIn {
        const val TITLE = "title"
        const val DESCRIPTION = "description"
        const val CONTENT = "content"
        const val TITLE_CONTENT = "title,content"
        val ALL = null // Default - searches all fields
    }

    object Language {
        const val ENGLISH = "en"
        const val SPANISH = "es"
        const val FRENCH = "fr"
        const val GERMAN = "de"
        const val ITALIAN = "it"
        const val PORTUGUESE = "pt"
        const val RUSSIAN = "ru"
        const val CHINESE = "zh"
    }
}