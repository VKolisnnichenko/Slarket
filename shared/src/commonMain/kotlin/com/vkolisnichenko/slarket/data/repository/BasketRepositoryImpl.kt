package com.vkolisnichenko.slarket.data.repository

import com.vkolisnichenko.slarket.data.database.AppDatabase
import com.vkolisnichenko.slarket.domain.entity.DishEntity
import com.vkolisnichenko.slarket.domain.repository.BasketRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class BasketRepositoryImpl : BasketRepository, KoinComponent {

    private val appDatabase: AppDatabase by inject()

    override suspend fun addBasketDish(dishEntity: DishEntity) {
        appDatabase.getBasketDao().insertDish(dishEntity)
    }

    override suspend fun updateBasket(dishEntity: DishEntity) {
        appDatabase.getBasketDao().update(dishEntity)
    }

    override suspend fun clearBasket() {
        appDatabase.getBasketDao().deleteAll()
    }

    override suspend fun getBasketItems(): List<DishEntity> {
        return appDatabase.getBasketDao().getAll()
    }

    override suspend fun deleteBasketItem(id: Long) {
        appDatabase.getBasketDao().deleteById(id)
    }
}