package com.st10361554.prog7314_ice_task_4.models

import com.google.gson.annotations.SerializedName

data class NewsResponse(
    @SerializedName("status")
    val status: String,

    @SerializedName("totalResults")
    val totalResults: Int,

    @SerializedName("articles")
    val articles: List<Article>
)
