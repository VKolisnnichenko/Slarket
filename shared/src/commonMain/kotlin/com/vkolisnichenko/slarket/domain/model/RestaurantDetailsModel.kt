package com.vkolisnichenko.slarket.domain.model

data class RestaurantDetailsModel(
     val restId : Int,
     val restName : String,
     val restDescription : String,
     val headerImageUrl : String,
     val pizzaList : List<PizzaModel>,
     var showBasket : Boolean = false,
     var displayBasketDialog: Boolean = false,
     var basketItemModel: BasketItemModel = BasketItemModel(),
     var isFavourite : Boolean = false
)