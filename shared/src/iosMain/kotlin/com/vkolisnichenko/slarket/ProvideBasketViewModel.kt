package com.vkolisnichenko.slarket

import com.vkolisnichenko.slarket.presentation.basket.BasketViewModel
import org.koin.core.component.inject

class ProvideBasketViewModel : ProvideViewModel() {
    override fun provideViewModel(): PlatformViewModel {
        val basketViewModel : BasketViewModel by inject()
        return basketViewModel
    }
}