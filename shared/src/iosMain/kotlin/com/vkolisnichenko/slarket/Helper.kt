package com.vkolisnichenko.slarket

import com.vkolisnichenko.slarket.data.di.platformModule
import com.vkolisnichenko.slarket.data.di.repositoriesModule
import com.vkolisnichenko.slarket.data.di.useCaseModule
import com.vkolisnichenko.slarket.data.di.viewModelModule
import org.koin.core.context.startKoin

fun initKoin(){
    startKoin {
        modules(listOf(useCaseModule, repositoriesModule, viewModelModule, platformModule()))
    }
}
