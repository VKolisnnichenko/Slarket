package com.vkolisnichenko.slarket.data.di

import com.vkolisnichenko.slarket.data.repository.BasketRepositoryImpl
import com.vkolisnichenko.slarket.data.repository.FavouriteRestaurantsRepositoryImpl
import com.vkolisnichenko.slarket.data.repository.PizzaListRepositoryImpl
import com.vkolisnichenko.slarket.domain.repository.BasketRepository
import com.vkolisnichenko.slarket.domain.repository.FavouriteRestaurantsRepository
import com.vkolisnichenko.slarket.domain.repository.PizzaListRepository
import com.vkolisnichenko.slarket.domain.usecases.AddBasketDishUseCase
import com.vkolisnichenko.slarket.domain.usecases.ClearBasketUseCase
import com.vkolisnichenko.slarket.domain.usecases.DeleteBasketItemUseCase
import com.vkolisnichenko.slarket.domain.usecases.DeleteFavouriteRestaurantUseCase
import com.vkolisnichenko.slarket.domain.usecases.GetBasketItemsUseCase
import com.vkolisnichenko.slarket.domain.usecases.GetFavouriteRestaurantsUseCase
import com.vkolisnichenko.slarket.domain.usecases.GetPizzaByIdUseCase
import com.vkolisnichenko.slarket.domain.usecases.GetPizzaListUseCase
import com.vkolisnichenko.slarket.domain.usecases.InsertFavouriteRestaurantUseCase
import com.vkolisnichenko.slarket.presentation.basket.BasketViewModel
import com.vkolisnichenko.slarket.presentation.details.RestaurantDetailsViewModel
import com.vkolisnichenko.slarket.presentation.rests.PizzaListViewModel
import org.koin.core.module.Module
import org.koin.dsl.module


val useCaseModule = module {
    single { GetPizzaListUseCase() }
    single { GetPizzaByIdUseCase() }
    single { AddBasketDishUseCase() }
    single { GetBasketItemsUseCase() }
    single { ClearBasketUseCase() }
    single { DeleteBasketItemUseCase() }
    single { GetFavouriteRestaurantsUseCase() }
    single { InsertFavouriteRestaurantUseCase() }
    single { DeleteFavouriteRestaurantUseCase() }
}

val repositoriesModule = module {
    factory<PizzaListRepository> { PizzaListRepositoryImpl() }
    factory<BasketRepository> { BasketRepositoryImpl() }
    factory<FavouriteRestaurantsRepository> { FavouriteRestaurantsRepositoryImpl() }
}

val viewModelModule = module {
    factory { PizzaListViewModel() }
    factory { RestaurantDetailsViewModel() }
    factory { BasketViewModel() }
}

expect fun platformModule(): Module

