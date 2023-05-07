package soft.commons.config

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy

@Lazy
@Configuration
class CoroutineConfig(
    @Value("\${coroutine.io.db: 64}") private val ioDBParallelism: Int,
    @Value("\${coroutine.io.redis: 64}") private val ioRedisParallelism: Int,
    @Value("\${coroutine.io.logging: 64}") private val ioLoggingParallelism: Int,
    @Value("\${coroutine.io.async: 64}") private val ioAsyncParallelism: Int,
    @Value("\${coroutine.io.http: 64}") private val ioHttpParallelism: Int
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    @Lazy
    @Bean
    fun ioDB() = Dispatchers.IO.limitedParallelism(this.ioDBParallelism)

    @OptIn(ExperimentalCoroutinesApi::class)
    @Lazy
    @Bean
    fun ioRedis() = Dispatchers.IO.limitedParallelism(this.ioRedisParallelism)

    @OptIn(ExperimentalCoroutinesApi::class)
    @Lazy
    @Bean
    fun ioLogging() = Dispatchers.IO.limitedParallelism(this.ioLoggingParallelism)

    @OptIn(ExperimentalCoroutinesApi::class)
    @Lazy
    @Bean
    fun ioAsync() = Dispatchers.IO.limitedParallelism(this.ioAsyncParallelism)

    @OptIn(ExperimentalCoroutinesApi::class)
    @Lazy
    @Bean
    fun ioHttp() = Dispatchers.IO.limitedParallelism(this.ioHttpParallelism)
}