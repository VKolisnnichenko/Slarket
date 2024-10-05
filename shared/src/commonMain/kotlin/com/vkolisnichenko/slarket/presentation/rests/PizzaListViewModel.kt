package com.vkolisnichenko.slarket.presentation.rests

import com.vkolisnichenko.slarket.PlatformViewModel
import com.vkolisnichenko.slarket.domain.model.PizzaRestaurantItemModel
import com.vkolisnichenko.slarket.domain.repository.PizzaListRepository
import com.vkolisnichenko.slarket.domain.usecases.DeleteFavouriteRestaurantUseCase
import com.vkolisnichenko.slarket.domain.usecases.GetFavouriteRestaurantsUseCase
import com.vkolisnichenko.slarket.domain.usecases.GetPizzaListUseCase
import com.vkolisnichenko.slarket.domain.usecases.InsertFavouriteRestaurantUseCase
import com.vkolisnichenko.slarket.onFailure
import com.vkolisnichenko.slarket.onSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.inject

class PizzaListViewModel : PlatformViewModel() {
    private val _pizzaListState: MutableStateFlow<PizzaListState> =
        MutableStateFlow(PizzaListLoading)

    val pizzaListState: StateFlow<PizzaListState> get() = _pizzaListState

    private val getPizzaListUseCase: GetPizzaListUseCase by inject()
    private val pizzaListRepository: PizzaListRepository by inject()

    private val insertFavouriteRestaurantUseCase: InsertFavouriteRestaurantUseCase by inject()
    private val deleteFavouriteRestaurantUseCase: DeleteFavouriteRestaurantUseCase by inject()
    private val getFavouriteRestaurantsUseCase: GetFavouriteRestaurantsUseCase by inject()

    fun deleteFavouriteRestaurant(id: Long) {
        if (_pizzaListState.value is PizzaListSuccess) {
            scope.launch(Dispatchers.IO) {
                val result = deleteFavouriteRestaurantUseCase.run(id)
                result.onSuccess {
                    _pizzaListState.emit(PizzaListSuccess((pizzaListState.value as PizzaListSuccess).items.map {
                        if (it.id == id.toInt()) {
                            it.isFavourite = false
                        }
                        it
                    }))

                }
                result.onFailure {
                    _pizzaListState.emit(PizzaListError(it.message ?: "Something went wrong"))
                }
            }
        }
    }

    fun insertFavouriteRestaurant(restaurantItemModel: PizzaRestaurantItemModel) {

        scope.launch(Dispatchers.IO) {
            val result = insertFavouriteRestaurantUseCase.run(restaurantItemModel)
            result.onSuccess {
                _pizzaListState.emit(PizzaListSuccess((pizzaListState.value as PizzaListSuccess).items.map {
                    if (it.id == restaurantItemModel.id) {
                        it.isFavourite = true
                    }
                    it
                }))

            }
            result.onFailure {
                _pizzaListState.emit(PizzaListError(it.message ?: "Something went wrong"))
            }
        }
    }

    fun fetchData() {
        scope.launch(Dispatchers.IO) {
            _pizzaListState.emit(PizzaListLoading)

            val favourite = async { getFavouriteRestaurantsUseCase.run(Unit) }.await()
            val pizzaListResult = getPizzaListUseCase.run(pizzaListRepository)

            favourite.onSuccess { favouriteList ->
                pizzaListResult.onSuccess { pizzaList ->
                    pizzaList.forEach { pizza ->
                        pizza.isFavourite = favouriteList.any { fav -> fav.id == pizza.id }
                    }
                    _pizzaListState.emit(PizzaListSuccess(pizzaList))
                }
            }
            favourite.onFailure {
                pizzaListResult.onSuccess {
                    _pizzaListState.emit(PizzaListSuccess(it))
                }
            }

            pizzaListResult.onFailure {
                _pizzaListState.emit(PizzaListError(it.message ?: "Something went wrong"))
            }

        }
    }

    fun fetchFavouriteRests() {
        scope.launch(Dispatchers.IO) {
            val favourite = getFavouriteRestaurantsUseCase.run(Unit)
            favourite.onSuccess {
                it.forEach { rest ->
                    rest.isFavourite = true
                }
                if (it.isNotEmpty()) {
                    _pizzaListState.emit(PizzaListSuccess(it))
                } else {
                    _pizzaListState.emit(PizzaListEmpty)
                }
            }
            favourite.onFailure {
                _pizzaListState.emit(PizzaListError(it.message ?: "Something went wrong"))
            }
        }
    }
}
