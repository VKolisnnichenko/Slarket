package com.vkolisnichenko.slarket.domain.usecases.base

import com.vkolisnichenko.slarket.Either
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

abstract class BaseUseCase<out Type, in Params> : KoinComponent {

    abstract suspend fun run(params: Params): Either<Type>

    operator fun invoke(
        params: Params,
        scope: CoroutineScope = CoroutineScope(Job()),
        onResult: (Either<Type>) -> Unit = {}
    ) {
        scope.launch(Dispatchers.Main) {
            val deferred = async(Dispatchers.IO) {
                run(params)
            }
            onResult(deferred.await())
        }
    }
}