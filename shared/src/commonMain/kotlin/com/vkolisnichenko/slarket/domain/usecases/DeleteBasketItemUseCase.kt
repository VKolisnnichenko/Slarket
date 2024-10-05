package com.vkolisnichenko.slarket.domain.usecases

import com.vkolisnichenko.slarket.Either
import com.vkolisnichenko.slarket.domain.repository.BasketRepository
import com.vkolisnichenko.slarket.domain.usecases.base.BaseUseCase
import org.koin.core.component.inject

class DeleteBasketItemUseCase : BaseUseCase<Unit, Long>() {
    private val basketRepository: BasketRepository by inject()
    override suspend fun run(params: Long) =
        Either.success(basketRepository.deleteBasketItem(params))
}