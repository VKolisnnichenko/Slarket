package com.vkolisnichenko.slarket

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import org.koin.core.component.KoinComponent

actual open class PlatformViewModel : ViewModel() , KoinComponent{
    actual val scope: CoroutineScope
        get() = viewModelScope

    actual fun logMessage(tag: String, message: String) {
        Log.d(tag, message)
    }

    actual fun clear() {
    }
}