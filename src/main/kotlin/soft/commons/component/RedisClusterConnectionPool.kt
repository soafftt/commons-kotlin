package soft.commons.component

import io.lettuce.core.RedisException
import io.lettuce.core.cluster.RedisClusterClient
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection
import io.lettuce.core.codec.StringCodec
import io.lettuce.core.support.AsyncConnectionPoolSupport
import io.lettuce.core.support.BoundedAsyncPool
import io.lettuce.core.support.BoundedPoolConfig
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.future.await
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.springframework.beans.factory.DisposableBean
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component

@Lazy
@Component
class RedisClusterConnectionPool(
    @Value("\${redis.pool.maxTotal:10}") maxTotal: Int,
    @Value("\${redis.pool.minIdle:10}") maxIdle: Int,
    @Value("\${redis.pool.maxIdle:10}") minIdle: Int,
    private val redisClusterClient: RedisClusterClient,
    private val ioRedis: CoroutineDispatcher
): DisposableBean {
    private final val boundedAsyncPool: BoundedAsyncPool<StatefulRedisClusterConnection<String, String>> =
        run {
            this.redisClusterClient.partitions

            AsyncConnectionPoolSupport.createBoundedObjectPool(
                { this.redisClusterClient.connectAsync(StringCodec.UTF8) },
                BoundedPoolConfig.builder()
                    .maxTotal(maxTotal)
                    .maxIdle(maxIdle)
                    .minIdle(minIdle)
                    .build(),
                true
            )
        }

    override fun destroy() {
        runBlocking {
            withContext(ioRedis) {
                boundedAsyncPool.clearAsync().await()
            }
        }
    }

    // acquire
    suspend fun acquire(): StatefulRedisClusterConnection<String, String> =
        runCatching {
            withContext(ioRedis) {
                boundedAsyncPool.acquire().await()
            }
        }.getOrElse {
            throw RedisException(it.message, it)
        }


    // release
    suspend fun release(redisClusterConnect: StatefulRedisClusterConnection<String, String>) =
        runCatching {
            withContext(ioRedis) {
                boundedAsyncPool.release(redisClusterConnect).await()
            }
        }.getOrElse {
            throw RedisException(it.message, it)
        }

}