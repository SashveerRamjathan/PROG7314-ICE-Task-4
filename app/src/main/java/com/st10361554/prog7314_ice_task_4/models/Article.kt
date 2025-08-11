package com.st10361554.prog7314_ice_task_4.models

import com.google.gson.annotations.SerializedName

data class Article(
    @SerializedName("source")
    val source: Source,

    @SerializedName("author")
    val author: String?,

    @SerializedName("title")
    val title: String,

    @SerializedName("description")
    val description: String?,

    @SerializedName("url")
    val url: String,

    @SerializedName("urlToImage")
    val urlToImage: String?,

    @SerializedName("publishedAt")
    val publishedAt: String,

    @SerializedName("content")
    val content: String?
)
