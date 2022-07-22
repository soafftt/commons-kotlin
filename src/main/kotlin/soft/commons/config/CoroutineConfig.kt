package soft.commons.config

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component

@Component
@Configuration
class CoroutineConfig(
    @Value("\${coroutine.limitParallelism.db:128}") val dbCoroutineLimitParallelism: Int,
    @Value("\${coroutine.limitParallelism.dynamo:128}") val redisCoroutineLimitParallelism: Int,
    @Value("\${coroutine.limitParallelism.redis:128}") val dynamoCoroutineLimitParallelism: Int,
    @Value("\${coroutine.limitParallelism.redis:128}") val logCoroutineLimitParallelism: Int,
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    @Bean
    fun dbIO() = Dispatchers.IO.limitedParallelism(dbCoroutineLimitParallelism)

    @OptIn(ExperimentalCoroutinesApi::class)
    @Bean
    fun redisIO() = Dispatchers.IO.limitedParallelism(redisCoroutineLimitParallelism)

    @OptIn(ExperimentalCoroutinesApi::class)
    @Bean
    fun dynamoIO() = Dispatchers.IO.limitedParallelism(dynamoCoroutineLimitParallelism)

    @OptIn(ExperimentalCoroutinesApi::class)
    @Bean
    fun logIO() = Dispatchers.IO.limitedParallelism(logCoroutineLimitParallelism)
}