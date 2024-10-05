package com.vkolisnichenko.slarket.domain.model

data class PizzaModel(
    val pizzaName: String = "",
    val pizzaPrice: Number = 0,
    val pizzaImageUrl: String = "",
    val desc: String = "",
    val id: Int = (0..100).random(),
)