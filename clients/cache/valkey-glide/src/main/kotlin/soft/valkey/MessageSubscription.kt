package soft.soft.valkey

import glide.api.models.PubSubMessage
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import soft.soft.valkey.ValkeyMessageSubscription.Companion.logger

class MessageSubscription() : ValkeyMessageSubscription {

    override val channel: Channel<PubSubMessage> = Channel(capacity = 64, onBufferOverflow = BufferOverflow.SUSPEND)

    override fun failure(pubSubMessage: PubSubMessage, exception: Throwable?) {
        logger.error(
            "MessageChannelSend Error: channel=${pubSubMessage.channel}, message=${pubSubMessage.message}", exception
        )
    }

    override fun destroy() {
        channel.close()
    }
}