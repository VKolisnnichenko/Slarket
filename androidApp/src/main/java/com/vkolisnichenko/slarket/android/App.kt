package com.vkolisnichenko.slarket.android

import android.app.Application
import com.vkolisnichenko.slarket.data.di.platformModule
import com.vkolisnichenko.slarket.data.di.repositoriesModule
import com.vkolisnichenko.slarket.data.di.useCaseModule
import com.vkolisnichenko.slarket.data.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        val myAppModules = listOf(useCaseModule, repositoriesModule, viewModelModule, platformModule())

        startKoin{
            androidLogger()
            androidContext(this@App)
            modules(myAppModules)
        }
    }
}