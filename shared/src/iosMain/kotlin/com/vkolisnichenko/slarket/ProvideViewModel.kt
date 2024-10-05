package com.vkolisnichenko.slarket

import org.koin.core.component.KoinComponent

abstract class ProvideViewModel : KoinComponent {
    abstract fun provideViewModel() : PlatformViewModel
}