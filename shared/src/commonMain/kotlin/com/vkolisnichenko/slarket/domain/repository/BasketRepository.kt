package com.vkolisnichenko.slarket.domain.repository

import com.vkolisnichenko.slarket.domain.entity.DishEntity

interface BasketRepository {
    suspend fun addBasketDish(dishEntity: DishEntity)
    suspend fun updateBasket(dishEntity: DishEntity)
    suspend fun clearBasket()
    suspend fun getBasketItems() : List<DishEntity>
    suspend fun deleteBasketItem(id: Long)
}