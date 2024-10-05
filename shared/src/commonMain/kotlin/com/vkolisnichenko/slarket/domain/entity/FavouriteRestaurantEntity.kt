package com.vkolisnichenko.slarket.domain.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FavouriteRestaurantEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val restId : Int,
    val title : String,
    val messageDescription : String,
    val image : String
)