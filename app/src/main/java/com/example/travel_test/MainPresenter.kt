package com.example.travel_test

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainPresenter(private val view: MainActivity) : MainContract.Presenter {

    private val service: AttractionsService
    private val newservice: NewsService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.travel.taipei/open-api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        service = retrofit.create(AttractionsService::class.java)
        newservice = retrofit.create(NewsService::class.java)
    }

    override fun getAttractions(lang: String) {
        val call = service.getAttractions(lang, "application/json")

        call.enqueue(object : Callback<AttractionsResponse> {
            override fun onResponse(
                call: Call<AttractionsResponse>,
                response: Response<AttractionsResponse>
            ) {
                if (response.isSuccessful) {
                    val attractions = response.body()?.attractions ?: emptyList()
                    view.showAttractions(attractions)
                } else {
                    view.showAttractionsError("Failed to fetch data")
                }
            }

            override fun onFailure(call: Call<AttractionsResponse>, t: Throwable) {
                view.showAttractionsError("Failed to fetch data: ${t.message}")
            }
        })
    }

    override fun getNews(lang: String) {
        val call = newservice.getNews(lang, "application/json")

        call.enqueue(object : Callback<NewsResponse> {
            override fun onResponse(
                call: Call<NewsResponse>,
                response: Response<NewsResponse>
            ) {
                if (response.isSuccessful) {
                    val news = response.body()?.news ?: emptyList()
                    view.showNews(news)
                } else {
                    view.showNewsError("Failed to fetch data")
                }
            }

            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                view.showNewsError("Failed to fetch data: ${t.message}")
            }
        })
    }

}


