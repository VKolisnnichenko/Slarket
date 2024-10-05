package com.vkolisnichenko.slarket.data.repository

import com.vkolisnichenko.slarket.data.mockDataPizzaList
import com.vkolisnichenko.slarket.data.pizzaDetailsMockList
import com.vkolisnichenko.slarket.domain.model.PizzaRestaurantItemModel
import com.vkolisnichenko.slarket.domain.repository.PizzaListRepository

class PizzaListRepositoryImpl : PizzaListRepository {
    override fun getPizzaList(): List<PizzaRestaurantItemModel> = mockDataPizzaList

    override fun getPizzaById(id: Int) = pizzaDetailsMockList.first { it.restId == id }
}