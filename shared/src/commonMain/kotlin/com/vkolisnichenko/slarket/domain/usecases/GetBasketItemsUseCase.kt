package com.vkolisnichenko.slarket.domain.usecases

import com.vkolisnichenko.slarket.Either
import com.vkolisnichenko.slarket.domain.entity.DishEntity
import com.vkolisnichenko.slarket.domain.mapper.Mapper
import com.vkolisnichenko.slarket.domain.model.BasketItemModel
import com.vkolisnichenko.slarket.domain.repository.BasketRepository
import com.vkolisnichenko.slarket.domain.usecases.base.BaseUseCase
import org.koin.core.component.inject

class GetBasketItemsUseCase : BaseUseCase<List<BasketItemModel>, Unit>(),
    Mapper<DishEntity, BasketItemModel> {
    private val basketRepository: BasketRepository by inject()

    override suspend fun run(params: Unit) = Either.success(basketRepository.getBasketItems().map { map(it) })

    override fun map(input: DishEntity) = BasketItemModel(
        id = input.id,
        dishId = input.dishId,
        restId = input.restId,
        name = input.name,
        isSauces = input.isSauces,
        isCutlery = input.isCutlery,
        userWishes = input.userWishes,
        sum = input.sum,
        dishImage = input.dishImage
    )

}