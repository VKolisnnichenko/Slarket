package com.vkolisnichenko.slarket

import kotlinx.coroutines.CoroutineScope
import org.koin.core.component.KoinComponent

expect open class PlatformViewModel() : KoinComponent {
    val scope: CoroutineScope

    fun logMessage(tag: String, message: String)

    fun clear()
}