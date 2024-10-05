package com.vkolisnichenko.slarket.data.repository

import com.vkolisnichenko.slarket.data.database.AppDatabase
import com.vkolisnichenko.slarket.domain.entity.FavouriteRestaurantEntity
import com.vkolisnichenko.slarket.domain.repository.FavouriteRestaurantsRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class FavouriteRestaurantsRepositoryImpl : FavouriteRestaurantsRepository, KoinComponent {
    private val appDatabase: AppDatabase by inject()

    override suspend fun addFavouriteRestaurant(favouriteRestaurantEntity: FavouriteRestaurantEntity) {
        appDatabase.getFavouriteRestaurantsDao().insertFavouriteRestaurant(favouriteRestaurantEntity)
    }

    override suspend fun deleteFavouriteRestaurant(id: Long) {
        appDatabase.getFavouriteRestaurantsDao().deleteById(id)
    }

    override suspend fun getFavouriteRestaurants(): List<FavouriteRestaurantEntity> {
        return appDatabase.getFavouriteRestaurantsDao().getAll()
    }
}