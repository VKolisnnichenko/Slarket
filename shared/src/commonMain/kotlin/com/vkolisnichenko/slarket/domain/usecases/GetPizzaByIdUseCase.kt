package com.vkolisnichenko.slarket.domain.usecases

import com.vkolisnichenko.slarket.Either
import com.vkolisnichenko.slarket.domain.repository.PizzaListRepository
import com.vkolisnichenko.slarket.domain.usecases.base.BaseUseCase
import com.vkolisnichenko.slarket.domain.model.RestaurantDetailsModel
import org.koin.core.component.inject

class GetPizzaByIdUseCase : BaseUseCase<RestaurantDetailsModel, Int>() {
    private val pizzaListRepository: PizzaListRepository by inject()

    override suspend fun run(params: Int) = Either.success(pizzaListRepository.getPizzaById(params))
}