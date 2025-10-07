package soft.http.ktor.internal

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.asCoroutineDispatcher
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

internal object CoroutineUtil {
    private val _vtThread: ExecutorService = Executors.newVirtualThreadPerTaskExecutor()
    private val _vtDispatcher: CoroutineDispatcher = _vtThread.asCoroutineDispatcher()

    val vtDispatcher: CoroutineDispatcher
        get() = _vtDispatcher

    val vtThread: ExecutorService
        get() = _vtThread

    @OptIn(ExperimentalCoroutinesApi::class)
    fun ioCoroutineDispatcher(ioCount: Int): CoroutineDispatcher {
        return Dispatchers.IO.limitedParallelism(ioCount)
    }

    fun fixedThreadPool(count: Int): ExecutorService {
        return Executors.newFixedThreadPool(count)
    }
}