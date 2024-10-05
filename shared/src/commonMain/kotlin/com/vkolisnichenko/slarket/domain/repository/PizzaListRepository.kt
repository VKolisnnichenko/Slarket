package com.vkolisnichenko.slarket.domain.repository

import com.vkolisnichenko.slarket.domain.model.PizzaRestaurantItemModel
import com.vkolisnichenko.slarket.domain.model.RestaurantDetailsModel

interface PizzaListRepository {
    fun getPizzaList() : List<PizzaRestaurantItemModel>
    fun getPizzaById(id : Int) : RestaurantDetailsModel
}