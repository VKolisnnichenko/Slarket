package com.vkolisnichenko.slarket

import com.vkolisnichenko.slarket.presentation.rests.PizzaListViewModel
import org.koin.core.component.inject

class ProvidePizzaListViewModel : ProvideViewModel(){

    override fun provideViewModel(): PlatformViewModel {
        val pizzaListViewModel: PizzaListViewModel by inject()
        return pizzaListViewModel
    }

}