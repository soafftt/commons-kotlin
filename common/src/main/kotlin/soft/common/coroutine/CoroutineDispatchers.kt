package soft.common.coroutine

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import java.util.concurrent.Executors


data object CoroutineDispatchers {
    val virtualThreadExecutor = Executors.newSingleThreadExecutor()

    private val virtualThreadDispatcher = virtualThreadExecutor.asCoroutineDispatcher()
    val Dispatchers.VirtualThread: CoroutineDispatcher
        get() = virtualThreadDispatcher
}


