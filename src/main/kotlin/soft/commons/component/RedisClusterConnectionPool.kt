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
    @Value("\${database.redis.pool.maxTotal:10}") protected var maxTotal: Int,
    @Value("\${database.redis.pool.minIdle:10}") protected var maxIdle: Int,
    @Value("\${database.redis.pool.maxIdle:10}") protected var minIdle: Int,
    private var redisClusterClientDisableAuthReconnect: RedisClusterClient?
) : DisposableBean {
    private var boundedAsyncPool: BoundedAsyncPool<StatefulRedisClusterConnection<String, String>>? = null
    final var created: Boolean
        protected set

    init {
        if (redisClusterClientDisableAuthReconnect != null) {
            this.redisClusterClientDisableAuthReconnect?.partitions
            this.boundedAsyncPool = AsyncConnectionPoolSupport.createBoundedObjectPool(
                { this.redisClusterClientDisableAuthReconnect?.connectAsync(StringCodec.UTF8) },
                BoundedPoolConfig.builder()
                    .maxIdle(maxIdle)
                    .maxTotal(maxTotal)
                    .minIdle(minIdle)
                    .testOnRelease(true)
                    .build(),
                true
            )
            this.created = true
        } else {
            this.created = false
        }
    }

    suspend fun acquire(hashTags: String): PooledConnection = try {
        this.boundedAsyncPool!!.acquire()!!.await().let {
            PooledConnection.create(this, it, hashTags)
        }
    } catch(ex: NoSuchElementException) {
        throw RedisException(ex.message, ex.cause)
    } catch(ex: Exception) {
        throw ex
    }

    fun release(connection: StatefulRedisClusterConnection<String, String>) = this.boundedAsyncPool?.release(connection)

    override fun destroy() {
        this.boundedAsyncPool?.closeAsync();
    }


    class PooledConnection private constructor(
        private var redisClusterConnectionPool: RedisClusterConnectionPool,
        private var statefulRedisClusterConnection: StatefulRedisClusterConnection<String, String>?,
        private var statefulRedisConnection: StatefulRedisConnection<String, String>
    ) {
        companion object {
            suspend fun create(
                redisClusterConnectionPool: RedisClusterConnectionPool,
                statefulRedisClusterConnection: StatefulRedisClusterConnection<String, String>?,
                hashTags: String = ""
            ): PooledConnection {
                val nodeId = statefulRedisClusterConnection!!.partitions
                    .getPartitionBySlot(SlotHash.getSlot(hashTags))
                    .nodeId
                val redisConnection= statefulRedisClusterConnection.getConnectionAsync(nodeId).await();

                return PooledConnection(redisClusterConnectionPool, statefulRedisClusterConnection, redisConnection)
            }
        }

        fun asyncCommands(): RedisAsyncCommands<String, String> = this.statefulRedisConnection.async()
        fun reactiveCommands(): RedisReactiveCommands<String, String> = this.statefulRedisConnection.reactive()
        fun close() = this.statefulRedisClusterConnection?.closeAsync()
        fun release() = this.redisClusterConnectionPool.release(this.statefulRedisClusterConnection!!)
    }
}