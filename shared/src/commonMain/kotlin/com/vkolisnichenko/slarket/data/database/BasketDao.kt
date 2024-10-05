package com.vkolisnichenko.slarket.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.vkolisnichenko.slarket.domain.entity.DishEntity


@Dao
interface BasketDao {
    @Insert( onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDish(dishEntity: DishEntity)

    @Query("SELECT * FROM DishEntity")
    suspend fun getAll() : List<DishEntity>

    @Update
    suspend fun update(dishEntity: DishEntity)

    @Query("DELETE FROM DishEntity")
    suspend fun deleteAll()

    @Query("DELETE FROM DishEntity WHERE id = :id")
    suspend fun deleteById(id: Long)

}