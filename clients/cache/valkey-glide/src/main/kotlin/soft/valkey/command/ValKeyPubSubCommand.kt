package soft.soft.valkey.command

import glide.api.GlideClient
import glide.api.commands.PubSubBaseCommands
import glide.api.models.Batch
import soft.soft.valkey.ValkeyBaseCommands
import soft.soft.valkey.ValkeyMessageSubscription

class ValKeyPubSubCommand private constructor(
    val commands: PubSubBaseCommands,
    val messageSubscriber: ValkeyMessageSubscription,
    glideClient: GlideClient
) : ValkeyBaseCommands(glideClient) {
    companion object {
        fun from(
            glideClient: GlideClient,
            messageSubscriber: ValkeyMessageSubscription
        ): ValKeyPubSubCommand {
            return ValKeyPubSubCommand(
                glideClient as PubSubBaseCommands,
                messageSubscriber,
                glideClient
            )
        }
    }

    override suspend fun pipeline(timeout: Int, prepare: (Batch) -> Unit): Array<Any> {
        throw UnsupportedOperationException("ValKeyPubSubCommand does not support pipelining")
    }
}