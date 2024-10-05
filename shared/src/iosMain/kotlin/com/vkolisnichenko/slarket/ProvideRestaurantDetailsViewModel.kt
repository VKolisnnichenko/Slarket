package com.vkolisnichenko.slarket
import com.vkolisnichenko.slarket.presentation.details.RestaurantDetailsViewModel
import org.koin.core.component.inject

class ProvideRestaurantDetailsViewModel : ProvideViewModel() {
    override fun provideViewModel(): PlatformViewModel {
        val restaurantDetailsViewModel : RestaurantDetailsViewModel by inject()
        return restaurantDetailsViewModel
    }
}