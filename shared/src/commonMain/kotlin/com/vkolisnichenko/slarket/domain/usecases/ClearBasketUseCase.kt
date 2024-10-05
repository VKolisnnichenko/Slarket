package com.vkolisnichenko.slarket.domain.usecases

import com.vkolisnichenko.slarket.Either
import com.vkolisnichenko.slarket.domain.repository.BasketRepository
import com.vkolisnichenko.slarket.domain.usecases.base.BaseUseCase
import org.koin.core.component.inject

class ClearBasketUseCase : BaseUseCase<Unit, Unit>() {
    private val basketRepository: BasketRepository by inject()

    override suspend fun run(params: Unit) = Either.success(basketRepository.clearBasket())
}