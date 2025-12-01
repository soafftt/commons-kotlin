package soft.valkey

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import soft.soft.valkey.command.ValKeyPubSubCommand
import soft.soft.valkey.config.GlideStandAlonePubSubRegistrar
import soft.soft.valkey.config.GlideStandAloneRegister

@Configuration
@Import(GlideStandAlonePubSubRegistrar::class)
class ValKeyConfig

@Component
class ValkeySub(
    private val valkeyPubSubCommand: ValKeyPubSubCommand
) {
    @EventListener(ApplicationReadyEvent::class)
    suspend fun handle(event: ApplicationReadyEvent) {
        withContext(Dispatchers.IO) {
            launch {
                valkeyPubSubCommand.messageSubscriber.receive { message ->
                    println(message)
                }
            }
        }
    }
}