package soft.r2dbc.core

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.asCoroutineDispatcher
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

object CoroutineDispatchers {
    private val virtualThreadExecutor: ExecutorService = Executors.newVirtualThreadPerTaskExecutor()
    private val virtualThreadDispatcher = virtualThreadExecutor.asCoroutineDispatcher()

    val Dispatchers.VT: CoroutineDispatcher
        get() = virtualThreadDispatcher

    @OptIn(ExperimentalCoroutinesApi::class)
    fun ioCoroutineDispatcher(ioCount: Int): CoroutineDispatcher {
        return Dispatchers.IO.limitedParallelism(ioCount)
    }

    fun fixedThreadPool(count: Int): ExecutorService {
        return Executors.newFixedThreadPool(count)
    }
}