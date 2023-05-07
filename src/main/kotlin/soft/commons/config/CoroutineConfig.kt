package soft.commons.config

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy

@Lazy
@Configuration
class CoroutineConfig(
    @Value("\${coroutine.io.common: 64}") private val ioCommonParallelism: Int,
    @Value("\${coroutine.io.db: 64}") private val ioDBParallelism: Int,
    @Value("\${coroutine.io.redis: 64}") private val ioRedisParallelism: Int,
    @Value("\${coroutine.io.logging: 64}") private val ioLoggingParallelism: Int,
    @Value("\${coroutine.io.async: 64}") private val ioAsyncParallelism: Int,
    @Value("\${coroutine.io.http: 64}") private val ioHttpParallelism: Int
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    @Lazy
    @Bean
    fun ioCommon(): CoroutineDispatcher =
        Dispatchers.IO.limitedParallelism(this.ioCommonParallelism)

    @OptIn(ExperimentalCoroutinesApi::class)
    @Lazy
    @Bean
    fun ioDB(): CoroutineDispatcher =
        Dispatchers.IO.limitedParallelism(this.ioDBParallelism)

    @OptIn(ExperimentalCoroutinesApi::class)
    @Lazy
    @Bean
    fun ioRedis(): CoroutineDispatcher =
        Dispatchers.IO.limitedParallelism(this.ioRedisParallelism)

    @OptIn(ExperimentalCoroutinesApi::class)
    @Lazy
    @Bean
    fun ioLogging(): CoroutineDispatcher =
        Dispatchers.IO.limitedParallelism(this.ioLoggingParallelism)

    @OptIn(ExperimentalCoroutinesApi::class)
    @Lazy
    @Bean
    fun ioAsync(): CoroutineDispatcher =
        Dispatchers.IO.limitedParallelism(this.ioAsyncParallelism)

    @OptIn(ExperimentalCoroutinesApi::class)
    @Lazy
    @Bean
    fun ioHttp(): CoroutineDispatcher =
        Dispatchers.IO.limitedParallelism(this.ioHttpParallelism)
}