package soft.soft.valkey

import glide.api.models.PubSubMessage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.receiveAsFlow
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.DisposableBean

interface ValkeyMessageSubscription : DisposableBean {
    companion object {
        internal val logger = LoggerFactory.getLogger(ValkeyMessageSubscription::class.java)
    }

    val channel: Channel<PubSubMessage>

    fun messageCallback(pubSubMessage: PubSubMessage, context: Any?) {
        val sendResult = channel.trySend(pubSubMessage)
        if (!sendResult.isSuccess) {
            failure(pubSubMessage, sendResult.exceptionOrNull())
        }
    }

    fun failure(pubSubMessage: PubSubMessage, exception: Throwable?)

    suspend fun receive(
        bufferSize: Int = 64,
        onBufferOverflow: BufferOverflow = BufferOverflow.SUSPEND,
        flowOnDispatcher: CoroutineDispatcher = Dispatchers.IO,
        receiver: suspend (pubSubMessage: PubSubMessage) -> Unit
    ) = channel.receiveAsFlow()
        .buffer(bufferSize, onBufferOverflow)
        .flowOn(flowOnDispatcher)
        .collect { receiver(it) }
}