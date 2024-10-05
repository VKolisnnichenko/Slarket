package com.vkolisnichenko.slarket.domain.usecases

import com.vkolisnichenko.slarket.Either
import com.vkolisnichenko.slarket.domain.entity.DishEntity
import com.vkolisnichenko.slarket.domain.mapper.Mapper
import com.vkolisnichenko.slarket.domain.model.BasketItemModel
import com.vkolisnichenko.slarket.domain.repository.BasketRepository
import com.vkolisnichenko.slarket.domain.usecases.base.BaseUseCase
import org.koin.core.component.inject

class AddBasketDishUseCase : BaseUseCase<Unit, BasketItemModel>(),
    Mapper<BasketItemModel, DishEntity> {
    private val basketRepository: BasketRepository by inject()

    override suspend fun run(params: BasketItemModel) =
        Either.success(basketRepository.addBasketDish(map(params)))

    override fun map(input: BasketItemModel): DishEntity {
        val dishEntity = DishEntity(
            restId = input.restId,
            dishId = input.dishId,
            name = input.name,
            isCutlery = input.isCutlery,
            isSauces = input.isSauces,
            userWishes = input.userWishes,
            sum = input.sum,
            dishImage = input.dishImage
        )
        return dishEntity
    }
}