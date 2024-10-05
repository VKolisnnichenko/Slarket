package com.vkolisnichenko.slarket.domain.repository

import com.vkolisnichenko.slarket.domain.entity.FavouriteRestaurantEntity

interface FavouriteRestaurantsRepository {
    suspend fun getFavouriteRestaurants(): List<FavouriteRestaurantEntity>
    suspend fun addFavouriteRestaurant(favouriteRestaurantEntity: FavouriteRestaurantEntity)
    suspend fun deleteFavouriteRestaurant(id: Long)
}