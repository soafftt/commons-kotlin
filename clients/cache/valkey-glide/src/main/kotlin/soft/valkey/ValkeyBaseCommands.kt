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

    /**
     * pipeline 명령어 수행.
     *  redis#lettuce 의 autoFlush 및 비동기 일괄 Flsh 방식이 아니가 때붐에.
     *  Get / Set 류의 모든 결과가 포함됩니다.
     *
     *  사용자는 자신이 수행한 명령어의 index 를 기억하여 해당 데이터만 꺼내는 불편함을 가질 수 있습니다.
     */
    open suspend fun pipeline(
        timeout: Int = 1000,
        riseError: Boolean = true,
        prepare: (Batch) -> Unit
    ) : Array<Any> {
        with(Batch(false)) {
            prepare(this)

            return glideClient.exec(
                this,
                riseError,
                BatchOptions.builder()
                    .timeout(timeout)
                    .build()
            ).await()
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