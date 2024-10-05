package com.vkolisnichenko.slarket.presentation.basket

import com.vkolisnichenko.slarket.PlatformViewModel
import com.vkolisnichenko.slarket.domain.model.BasketPresentationModel
import com.vkolisnichenko.slarket.domain.usecases.DeleteBasketItemUseCase
import com.vkolisnichenko.slarket.domain.usecases.GetBasketItemsUseCase
import com.vkolisnichenko.slarket.onFailure
import com.vkolisnichenko.slarket.onSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.inject

class BasketViewModel : PlatformViewModel() {
    private val _basketState = MutableStateFlow<BasketState>(BasketStateLoading)
    val basketState : StateFlow<BasketState> = _basketState

    private val getBasketItemsUseCase: GetBasketItemsUseCase by inject()
    private val deleteBasketItemUseCase: DeleteBasketItemUseCase by inject()


    fun fetchBasketItems() {
        scope.launch(Dispatchers.IO) {
            _basketState.emit(BasketStateLoading)
            val result = getBasketItemsUseCase.run(Unit)
            result.onSuccess {
                if (it.isEmpty()) {
                    _basketState.emit(BasketStateEmpty)
                } else {
                    _basketState.emit(
                        BasketStateSuccess(
                            BasketPresentationModel(
                                basketItems = it,
                                totalCost = it.sumOf { item -> item.sum.toDouble() })
                        )
                    )
                }
            }
            result.onFailure {
                _basketState.emit(BasketStateError("Something went wrong"))
            }
        }
    }

    fun deleteBasketItem(id: Long) {
        scope.launch(Dispatchers.IO) {
            val result = deleteBasketItemUseCase.run(id)
            result.onSuccess {
                fetchBasketItems()
            }
            result.onFailure {
                _basketState.emit(BasketStateError("Something went wrong"))
            }
        }
    }

}