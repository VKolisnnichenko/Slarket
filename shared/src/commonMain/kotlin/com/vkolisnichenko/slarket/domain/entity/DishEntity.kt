package com.vkolisnichenko.slarket.domain.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DishEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val restId : Int,
    val dishId : Int,
    val name : String,
    val isCutlery : Boolean,
    val isSauces : Boolean,
    val userWishes : String,
    val sum : Int,
    val dishImage : String
)
