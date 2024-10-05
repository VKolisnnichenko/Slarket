package com.vkolisnichenko.slarket.domain.model


data class BasketItemModel(
    var id : Long = 0,
    var restId : Int = 0,
    var dishId : Int = 0,
    var name : String = "",
    var isCutlery : Boolean = false,
    var isSauces : Boolean = false,
    var userWishes : String = "",
    var sum : Int = 0,
    var dishImage : String = ""
)