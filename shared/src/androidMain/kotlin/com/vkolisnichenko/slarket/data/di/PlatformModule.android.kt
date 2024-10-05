package com.vkolisnichenko.slarket.data.di

import com.vkolisnichenko.slarket.data.database.AppDatabase
import com.vkolisnichenko.slarket.getDatabase
import org.koin.dsl.module


actual fun platformModule() = module {
    single<AppDatabase> { getDatabase(get()) }
}