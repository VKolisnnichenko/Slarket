package com.vkolisnichenko.slarket.domain.usecases

import com.vkolisnichenko.slarket.Either
import com.vkolisnichenko.slarket.domain.model.PizzaRestaurantItemModel
import com.vkolisnichenko.slarket.domain.repository.PizzaListRepository
import com.vkolisnichenko.slarket.domain.usecases.base.BaseUseCase

class GetPizzaListUseCase : BaseUseCase<List<PizzaRestaurantItemModel>, PizzaListRepository>() {

    override suspend fun run(params: PizzaListRepository): Either<List<PizzaRestaurantItemModel>> {
        return Either.success(params.getPizzaList())
    }
}