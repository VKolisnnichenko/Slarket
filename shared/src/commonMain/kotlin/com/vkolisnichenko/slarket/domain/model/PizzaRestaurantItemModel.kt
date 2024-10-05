package com.vkolisnichenko.slarket.domain.model

data class PizzaRestaurantItemModel(
    val id: Int,
    val title: String,
    val messageDescription: String,
    val image: String,
    var isFavourite: Boolean = false
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PizzaRestaurantItemModel) return false

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
