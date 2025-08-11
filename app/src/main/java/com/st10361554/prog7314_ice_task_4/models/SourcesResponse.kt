package com.st10361554.prog7314_ice_task_4.models

import com.google.gson.annotations.SerializedName

data class SourcesResponse(
    @SerializedName("status")
    val status: String,

    @SerializedName("sources")
    val sources: List<NewsSource>
)
