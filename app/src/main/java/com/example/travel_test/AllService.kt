package com.example.travel_test

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface AttractionsService {
    @GET("{lang}/Attractions/All")
    fun getAttractions(@Path("lang") lang: String, @Header("Accept") accept: String): Call<AttractionsResponse>
}

interface NewsService {
    @GET("{lang}/Events/News")
    fun getNews(@Path("lang") lang: String, @Header("Accept") accept: String): Call<NewsResponse>
}


