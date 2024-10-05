package com.vkolisnichenko.slarket.presentation.details

import com.vkolisnichenko.slarket.domain.model.RestaurantDetailsModel

abstract class RestaurantDetailsState

object RestDetailsStateLoading : RestaurantDetailsState()
class RestDetailsStateError(val message: String) : RestaurantDetailsState()
class RestDetailsStateSuccess(val restaurantDetailsModel: RestaurantDetailsModel) : RestaurantDetailsState()