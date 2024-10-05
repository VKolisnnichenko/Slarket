package com.vkolisnichenko.slarket.domain.usecases

import com.vkolisnichenko.slarket.Either
import com.vkolisnichenko.slarket.domain.entity.FavouriteRestaurantEntity
import com.vkolisnichenko.slarket.domain.mapper.Mapper
import com.vkolisnichenko.slarket.domain.model.PizzaRestaurantItemModel
import com.vkolisnichenko.slarket.domain.repository.FavouriteRestaurantsRepository
import com.vkolisnichenko.slarket.domain.usecases.base.BaseUseCase
import org.koin.core.component.inject

class GetFavouriteRestaurantsUseCase : BaseUseCase<List<PizzaRestaurantItemModel>, Unit>(),
    Mapper<FavouriteRestaurantEntity, PizzaRestaurantItemModel> {
    private val favouriteRestaurantsRepository: FavouriteRestaurantsRepository by inject()

    override suspend fun run(params: Unit): Either<List<PizzaRestaurantItemModel>> = Either.success(
            favouriteRestaurantsRepository.getFavouriteRestaurants().map { map(it) })


    override fun map(input: FavouriteRestaurantEntity) = PizzaRestaurantItemModel(
        id = input.restId,
        title = input.title,
        messageDescription = input.messageDescription,
        image = input.image,
    )
}