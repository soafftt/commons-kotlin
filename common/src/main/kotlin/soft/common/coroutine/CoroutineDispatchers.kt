package soft.common.coroutine

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


data object CoroutineDispatchers {
    val virtualThreadExecutor: ExecutorService = Executors.newVirtualThreadPerTaskExecutor()

    private val virtualThreadDispatcher = virtualThreadExecutor.asCoroutineDispatcher()

    val Dispatchers.VT: CoroutineDispatcher
        get() = virtualThreadDispatcher
}


