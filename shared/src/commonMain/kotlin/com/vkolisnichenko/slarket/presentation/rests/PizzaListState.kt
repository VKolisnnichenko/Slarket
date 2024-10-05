package com.vkolisnichenko.slarket.presentation.rests

import com.vkolisnichenko.slarket.domain.model.PizzaRestaurantItemModel

abstract class PizzaListState

class PizzaListSuccess(val items: List<PizzaRestaurantItemModel>) : PizzaListState()
class PizzaListError(val message: String) : PizzaListState()
object PizzaListLoading : PizzaListState()
object PizzaListEmpty : PizzaListState()
