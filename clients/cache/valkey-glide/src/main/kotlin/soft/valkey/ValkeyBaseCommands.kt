package soft.soft.valkey

import glide.api.GlideClient
import glide.api.models.Batch
import glide.api.models.commands.batch.BatchOptions
import kotlinx.coroutines.future.await
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.DisposableBean

abstract class ValkeyBaseCommands internal constructor(
    private val glideClient: GlideClient
) : DisposableBean {
    companion object {
        val logger = LoggerFactory.getLogger(ValkeyBaseCommands::class.java)
    }

    open suspend fun pipeline(
        timeout: Int = 1000,
        prepare: (Batch) -> Unit
    ) : Array<Any> {
        with(Batch(false)) {
            prepare(this)

            BatchOptions.builder()
                .timeout(timeout)
                .build()

            return glideClient.exec(this, true).await()
        }
    }

    override fun destroy() {
        try {
            glideClient.close()
        } catch (e: Exception) {
            logger.warn("Error during destroying command", e)
        }
    }
}