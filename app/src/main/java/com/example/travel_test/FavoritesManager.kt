package com.example.travel_test

object FavoritesManager {
    private val favoriteList = mutableListOf<Attraction>()

    fun addFavorite(attraction: Attraction) {
        if (!favoriteList.contains(attraction)) {
            favoriteList.add(attraction)
        }
    }

    fun removeFavorite(attraction: Attraction) {
        favoriteList.remove(attraction)
    }

    fun getFavorites(): List<Attraction> {
        return favoriteList
    }
}

