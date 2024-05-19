package com.example.travel_test

interface MainContract {
    interface View {
        fun showAttractions(attractions: List<Attraction>)
        fun showAttractionsError(message: String)
        fun showNews(news: List<NewsItem>)
        fun showNewsError(message: String)
    }

    interface Presenter {
        fun getAttractions(lang: String)
        fun getNews(lang: String)
    }
}
