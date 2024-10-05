package com.vkolisnichenko.slarket.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.vkolisnichenko.slarket.domain.entity.FavouriteRestaurantEntity

@Dao
interface FavouriteRestaurantsDao {
    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavouriteRestaurant(favouriteRestaurantEntity: FavouriteRestaurantEntity)

    @Query("SELECT * FROM FavouriteRestaurantEntity")
    suspend fun getAll() : List<FavouriteRestaurantEntity>

    @Update
    suspend fun update(favouriteRestaurantEntity: FavouriteRestaurantEntity)

    @Query("DELETE FROM FavouriteRestaurantEntity")
    suspend fun deleteAll()

    @Query("DELETE FROM FavouriteRestaurantEntity WHERE restId = :restId")
    suspend fun deleteById(restId: Long)
}