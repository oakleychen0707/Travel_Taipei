package com.example.travel_test

import com.google.gson.annotations.SerializedName

data class NewsResponse (
    @SerializedName("data") val news: List<NewsItem>
    )

data class NewsItem(
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("posted") val posted: String,
    @SerializedName("modified") val modified: String,
    @SerializedName("url") val url: String,
)