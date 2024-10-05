package com.vkolisnichenko.slarket.domain.usecases

import com.vkolisnichenko.slarket.Either
import com.vkolisnichenko.slarket.domain.repository.FavouriteRestaurantsRepository
import com.vkolisnichenko.slarket.domain.usecases.base.BaseUseCase
import org.koin.core.component.inject

class DeleteFavouriteRestaurantUseCase : BaseUseCase<Unit, Long>() {
    private val favouriteRestaurantsRepository: FavouriteRestaurantsRepository by inject()
    override suspend fun run(params: Long) =
        Either.success(favouriteRestaurantsRepository.deleteFavouriteRestaurant(params))
}