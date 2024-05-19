package com.example.travel_test

import com.google.gson.annotations.SerializedName

data class AttractionsResponse(
    @SerializedName("data") val attractions: List<Attraction>
)

data class Attraction(
    @SerializedName("name") val name: String,
    @SerializedName("introduction") val introduction: String,
    @SerializedName("address") val address: String,
    @SerializedName("tel") val tel: String,
    @SerializedName("fax") val fax: String,
    @SerializedName("email") val email: String,
    @SerializedName("official_site") val official_site: String,
    @SerializedName("facebook") val facebook: String,
    @SerializedName("images") val images: List<Image>,
)

data class Image(
    @SerializedName("src") val src: String,
    @SerializedName("subject") val subject: String,
    @SerializedName("ext") val ext: String
)
