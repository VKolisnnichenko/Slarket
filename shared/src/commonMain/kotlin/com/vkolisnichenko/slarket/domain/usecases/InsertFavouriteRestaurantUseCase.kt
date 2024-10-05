package com.vkolisnichenko.slarket.domain.usecases

import com.vkolisnichenko.slarket.Either
import com.vkolisnichenko.slarket.domain.entity.FavouriteRestaurantEntity
import com.vkolisnichenko.slarket.domain.mapper.Mapper
import com.vkolisnichenko.slarket.domain.model.PizzaRestaurantItemModel
import com.vkolisnichenko.slarket.domain.repository.FavouriteRestaurantsRepository
import com.vkolisnichenko.slarket.domain.usecases.base.BaseUseCase
import org.koin.core.component.inject

class InsertFavouriteRestaurantUseCase : BaseUseCase<Unit, PizzaRestaurantItemModel>(),
    Mapper<PizzaRestaurantItemModel, FavouriteRestaurantEntity> {
    private val favouriteRestaurantsRepository: FavouriteRestaurantsRepository by inject()
    override suspend fun run(params: PizzaRestaurantItemModel) =
        Either.success(favouriteRestaurantsRepository.addFavouriteRestaurant(map(params)))


    override fun map(input: PizzaRestaurantItemModel): FavouriteRestaurantEntity {
        return FavouriteRestaurantEntity(
            restId = input.id,
            title = input.title,
            messageDescription = input.messageDescription,
            image = input.image
        )
    }
}