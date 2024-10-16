package com.vkolisnichenko.slarket.presentation.details

import com.vkolisnichenko.slarket.PlatformViewModel
import com.vkolisnichenko.slarket.data.mockDataPizzaList
import com.vkolisnichenko.slarket.domain.model.BasketItemModel
import com.vkolisnichenko.slarket.domain.model.PizzaModel
import com.vkolisnichenko.slarket.domain.model.RestaurantDetailsModel
import com.vkolisnichenko.slarket.domain.usecases.AddBasketDishUseCase
import com.vkolisnichenko.slarket.domain.usecases.ClearBasketUseCase
import com.vkolisnichenko.slarket.domain.usecases.DeleteFavouriteRestaurantUseCase
import com.vkolisnichenko.slarket.domain.usecases.GetBasketItemsUseCase
import com.vkolisnichenko.slarket.domain.usecases.GetFavouriteRestaurantsUseCase
import com.vkolisnichenko.slarket.domain.usecases.GetPizzaByIdUseCase
import com.vkolisnichenko.slarket.domain.usecases.InsertFavouriteRestaurantUseCase
import com.vkolisnichenko.slarket.onFailure
import com.vkolisnichenko.slarket.onSuccess
import com.vkolisnichenko.slarket.resources.SharedResources
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.inject
import kotlin.math.log

class RestaurantDetailsViewModel : PlatformViewModel() {
    private val _restDetailsState: MutableStateFlow<RestaurantDetailsState> =
        MutableStateFlow(RestDetailsStateLoading)
    val restDetailsState: StateFlow<RestaurantDetailsState> = _restDetailsState

    private val getPizzaByIdUseCase: GetPizzaByIdUseCase by inject()

    private val addBasketDishUseCase: AddBasketDishUseCase by inject()
    private val getBasketItemsUseCase: GetBasketItemsUseCase by inject()
    private val clearBasketUseCase: ClearBasketUseCase by inject()

    private val insertFavouriteRestaurantUseCase: InsertFavouriteRestaurantUseCase by inject()
    private val deleteFavouriteRestaurantUseCase: DeleteFavouriteRestaurantUseCase by inject()
    private val getFavouriteRestaurantsUseCase: GetFavouriteRestaurantsUseCase by inject()


    fun fetchRestDetails(restId: Int) {
        scope.launch(Dispatchers.IO) {
            _restDetailsState.emit(RestDetailsStateLoading)
            val result = getPizzaByIdUseCase.run(restId)
            result.onSuccess {
                isCurrentRestFavourite(it)
                _restDetailsState.emit(RestDetailsStateSuccess(it))
                clear()
            }
            result.onFailure {
                _restDetailsState.emit(RestDetailsStateError("Something went wrong"))
                clear()
            }
        }
    }

    private suspend fun isCurrentRestFavourite(restaurantDetailsModel: RestaurantDetailsModel) {
        val favouriteRests = getFavouriteRestaurantsUseCase.run(Unit)
        favouriteRests.onSuccess {
            restaurantDetailsModel.isFavourite = it.any { it.id == restaurantDetailsModel.restId }
        }
    }

    fun addToFavourite() {
        scope.launch(Dispatchers.IO) {
            if (restDetailsState.value !is RestDetailsStateSuccess) return@launch
            val model = (restDetailsState.value as RestDetailsStateSuccess).restaurantDetailsModel
            scope.launch(Dispatchers.IO) {
                insertFavouriteRestaurantUseCase.run(mockDataPizzaList.first { it.id == model.restId })
                _restDetailsState.emit(
                    RestDetailsStateSuccess(
                        (_restDetailsState.value as RestDetailsStateSuccess).restaurantDetailsModel.copy(
                            isFavourite = true
                        )
                    )
                )
            }
        }
    }

    fun removeFromFavourite() {
        scope.launch(Dispatchers.IO) {
            if (restDetailsState.value !is RestDetailsStateSuccess) return@launch
            val model = (restDetailsState.value as RestDetailsStateSuccess).restaurantDetailsModel
            scope.launch {
                deleteFavouriteRestaurantUseCase.run(model.restId.toLong())
                _restDetailsState.emit(
                    RestDetailsStateSuccess(
                        (_restDetailsState.value as RestDetailsStateSuccess).restaurantDetailsModel.copy(
                            isFavourite = false
                        )
                    )
                )
            }
        }
    }

    fun addToBasket() {
        scope.launch {
            if (restDetailsState.value !is RestDetailsStateSuccess) return@launch
            val model = (restDetailsState.value as RestDetailsStateSuccess).restaurantDetailsModel
            model.showBasket = true
            scope.launch(Dispatchers.IO) {
                checkPreviousBasketItem(model) {
                    if (it) {
                        addBasketDishUseCase.run(model.basketItemModel)
                    } else {
                        model.displayBasketDialog = true
                    }
                }
            }
        }
    }

    fun hideBasketDialog() {
        if (restDetailsState.value is RestDetailsStateSuccess) {
            val model = (restDetailsState.value as RestDetailsStateSuccess).restaurantDetailsModel
            model.displayBasketDialog = false
            scope.launch {
                _restDetailsState.emit(RestDetailsStateSuccess(model))
            }
        }
    }

    fun replaceBasketItem() {
        if (restDetailsState.value is RestDetailsStateSuccess)
            scope.launch {
                val model =
                    (restDetailsState.value as RestDetailsStateSuccess).restaurantDetailsModel
                scope.launch(Dispatchers.IO) {
                    clearBasketUseCase.run(Unit)
                    addBasketDishUseCase.run(model.basketItemModel)
                    model.displayBasketDialog = false
                    _restDetailsState.emit(RestDetailsStateSuccess(model))
                }
            }
    }

    fun resetBasketModel(pizzaModel: PizzaModel) {
        if (restDetailsState.value is RestDetailsStateSuccess) {
            val model = (restDetailsState.value as RestDetailsStateSuccess).restaurantDetailsModel
            model.basketItemModel = BasketItemModel()
            model.basketItemModel.apply {
                restId = model.restId
                name = pizzaModel.pizzaName
                dishId = pizzaModel.id
                sum = pizzaModel.pizzaPrice.toInt()
            }
            scope.launch {
                _restDetailsState.emit(RestDetailsStateSuccess(model))
            }
        }
    }

    fun updateSelectedDish(isSelected: Boolean) {
        scope.launch(Dispatchers.IO) {
            if (restDetailsState.value is RestDetailsStateSuccess) {
                val model =
                    (restDetailsState.value as RestDetailsStateSuccess).restaurantDetailsModel
                if (isSelected) {
                    model.basketItemModel.sum += 1;
                } else {
                    model.basketItemModel.sum -= 1;
                }
                _restDetailsState.emit(RestDetailsStateSuccess(model))
            }
        }
    }

    fun isShowBasketFAB() {
        scope.launch(Dispatchers.IO) {
            if (restDetailsState.value is RestDetailsStateSuccess) {
                val model =
                    (restDetailsState.value as RestDetailsStateSuccess).restaurantDetailsModel
                val basketItems = getBasketItemsUseCase.run(Unit)
                basketItems.onSuccess {
                    model.showBasket = it.isNotEmpty()
                }
            }
        }
    }

    private suspend fun checkPreviousBasketItem(
        model: RestaurantDetailsModel,
        result: suspend (Boolean) -> Unit
    ) {
        val basketItems = getBasketItemsUseCase.run(Unit)
        basketItems.onSuccess {
            result.invoke(it.isEmpty() || it.last().restId == model.restId)
        }

        basketItems.onFailure {
            result.invoke(false)
        }
    }

}