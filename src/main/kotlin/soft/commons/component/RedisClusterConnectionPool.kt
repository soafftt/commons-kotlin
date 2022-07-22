package soft.commons.component

import io.lettuce.core.RedisException
import io.lettuce.core.api.StatefulRedisConnection
import io.lettuce.core.api.async.RedisAsyncCommands
import io.lettuce.core.api.reactive.RedisReactiveCommands
import io.lettuce.core.cluster.RedisClusterClient
import io.lettuce.core.cluster.SlotHash
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection
import io.lettuce.core.codec.StringCodec
import io.lettuce.core.support.AsyncConnectionPoolSupport
import io.lettuce.core.support.BoundedAsyncPool
import io.lettuce.core.support.BoundedPoolConfig
import kotlinx.coroutines.future.await
import org.springframework.beans.factory.DisposableBean
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.NoSuchElementException

// For RedisTransactionCommands
@Component
class RedisClusterConnectionPool(
    @Value("\${database.redis.pool.maxTotal:10}") maxTotal: Int,
    @Value("\${database.redis.pool.minIdle:10}") maxIdle: Int,
    @Value("\${database.redis.pool.maxIdle:10}") minIdle: Int,
    private var redisClusterClientDisableAuthReconnect: RedisClusterClient?
) : DisposableBean {
    private final val boundedAsyncPool: BoundedAsyncPool<StatefulRedisClusterConnection<String, String>>? =
        if (redisClusterClientDisableAuthReconnect != null) {
            this.redisClusterClientDisableAuthReconnect?.partitions

            AsyncConnectionPoolSupport.createBoundedObjectPool(
                { this.redisClusterClientDisableAuthReconnect?.connectAsync(StringCodec.UTF8) },
                BoundedPoolConfig.builder()
                    .maxIdle(maxIdle)
                    .maxTotal(maxTotal)
                    .minIdle(minIdle)
                    .testOnRelease(true)
                    .build(),
                true
            )
        } else {
            null
        }

    val created: Boolean
        = boundedAsyncPool != null

    suspend fun acquire(hashTags: String): PooledConnection =
        runCatching {
            this.boundedAsyncPool!!.acquire()!!.await().let {
                PooledConnection.of(this, it, hashTags)
            }
        }.onFailure {
            throw when(it) {
                is NoSuchElementException -> RedisException(it.message, it.cause)
                else -> it
            }
        }.getOrThrow()

    fun release(connection: StatefulRedisClusterConnection<String, String>) =
        this.boundedAsyncPool?.release(connection)

    override fun destroy() =
        this.boundedAsyncPool?.closeAsync().let { Unit }


    class PooledConnection private constructor(
        private var redisClusterConnectionPool: RedisClusterConnectionPool,
        private var statefulRedisClusterConnection: StatefulRedisClusterConnection<String, String>?,
        private var statefulRedisConnection: StatefulRedisConnection<String, String>
    ) {
        companion object {
            suspend fun of(
                redisClusterConnectionPool: RedisClusterConnectionPool,
                statefulRedisClusterConnection: StatefulRedisClusterConnection<String, String>?,
                hashTags: String
            ): PooledConnection {
                val redisConnection = statefulRedisClusterConnection!!.getConnectionAsync(
                    statefulRedisClusterConnection
                        .partitions
                        .getPartitionBySlot(SlotHash.getSlot(hashTags))
                        .nodeId
                ).await();

                return PooledConnection(redisClusterConnectionPool, statefulRedisClusterConnection, redisConnection)
            }
        }

        fun asyncCommands(): RedisAsyncCommands<String, String> =
            this.statefulRedisConnection.async()

        fun reactiveCommands(): RedisReactiveCommands<String, String> =
            this.statefulRedisConnection.reactive()

        fun close() =
            this.statefulRedisClusterConnection?.closeAsync()

        fun release() =
            this.redisClusterConnectionPool.release(this.statefulRedisClusterConnection!!)
    }
}