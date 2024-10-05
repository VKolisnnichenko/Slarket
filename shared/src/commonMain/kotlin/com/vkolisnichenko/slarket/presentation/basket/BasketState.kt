package com.vkolisnichenko.slarket.presentation.basket

import com.vkolisnichenko.slarket.domain.model.BasketPresentationModel

abstract class BasketState

object BasketStateLoading : BasketState()
object BasketStateEmpty : BasketState()
class BasketStateError(val message: String) : BasketState()
class BasketStateSuccess(val basketPresentationModel: BasketPresentationModel) : BasketState()