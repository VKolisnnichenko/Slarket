package com.vkolisnichenko.slarket.domain.model

data class BasketPresentationModel(
    val basketItems: List<BasketItemModel>,
    val totalCost: Double
)