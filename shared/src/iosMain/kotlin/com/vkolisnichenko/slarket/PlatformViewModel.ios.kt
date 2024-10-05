package com.vkolisnichenko.slarket

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.cancel
import org.koin.core.component.KoinComponent
import platform.Foundation.NSLog

actual open class PlatformViewModel : KoinComponent {
    actual val scope: CoroutineScope
    get() = CoroutineScope(Dispatchers.IO)

    actual fun clear() {
        scope.cancel()
    }

    actual fun logMessage(tag: String, message: String) {
        NSLog("$tag: $message")
    }
}
