package com.vkolisnichenko.slarket.data.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import com.vkolisnichenko.slarket.domain.entity.DishEntity
import com.vkolisnichenko.slarket.domain.entity.FavouriteRestaurantEntity

@Database(entities = [DishEntity::class, FavouriteRestaurantEntity::class], version = 2)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getBasketDao(): BasketDao
    abstract fun getFavouriteRestaurantsDao(): FavouriteRestaurantsDao
}
@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase>
