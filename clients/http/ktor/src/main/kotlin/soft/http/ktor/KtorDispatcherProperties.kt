package soft.http.ktor

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import java.util.concurrent.Executors

data class KtorDispatcherProperties(
    val ioCount: Int? = null,
    val useVtDispatcher: Boolean? = null,
) {
    init {
        if (ioCount == null && useVtDispatcher == null) {
            throw IllegalArgumentException("ioCount or useVtDispatcher must not be null")
        }

        ioCount?.apply {
            if (useVtDispatcher == true) {
                throw IllegalArgumentException("You must configure either ioCount or useVtDispatcher, not both.")
            }

            if (this <= 0) {
                throw IllegalArgumentException("ioCount must positive")
            }
        }
    }

    fun makeDispatcher(): CoroutineDispatcher {
        return if (ioCount != null) {
            Dispatchers.IO.limitedParallelism(ioCount)
        } else {
            Executors.newVirtualThreadPerTaskExecutor().asCoroutineDispatcher()
        }
    }
}