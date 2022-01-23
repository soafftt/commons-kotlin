package soft.commons.component

import kotlinx.coroutines.runBlocking
import soft.commons.config.RedisConfig
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("commons-test")
@SpringBootTest(classes = [RedisConfig::class, RedisClusterConnectionPool::class])
class RedisClusterConnectionPoolTest {

    private val hashTag = "{key:1}"

    @Autowired
    private lateinit var redisClusterConnectionPool: RedisClusterConnectionPool

    @Test
    fun test_dependencyInjection_whenExistsRedisConnectionProperty_createdPoolInstance(): Unit {
        val actualPoolCreated: Boolean = redisClusterConnectionPool.created

        Assertions.assertNotNull(this.redisClusterConnectionPool)
        Assertions.assertTrue(actualPoolCreated)
    }

    @Test
    fun test_acquire_success(): Unit {
        val actualConnection = runBlocking {
            redisClusterConnectionPool.acquire(hashTag)
        }

        Assertions.assertNotNull(actualConnection);
    }

    @Test
    fun test_asyncCommands_success() {
        val actualConnection = runBlocking {
            redisClusterConnectionPool.acquire(hashTag)
        }

        Assertions.assertNotNull(actualConnection);
        Assertions.assertNotNull(actualConnection.asyncCommands())
    }

    @Test
    fun test_reactiveCommands_success() {
        val actualConnection = runBlocking {
            redisClusterConnectionPool.acquire(hashTag)
        }

        Assertions.assertNotNull(actualConnection);
        Assertions.assertNotNull(actualConnection.reactiveCommands())
    }

    @Test
    fun test_release_close() {
        val actualConnection = runBlocking {
            redisClusterConnectionPool.acquire(hashTag)
        }

        actualConnection.close()
    }

    @Test
    fun test_release_success() {
        val actualConnection = runBlocking {
            redisClusterConnectionPool.acquire(hashTag)
        }

        actualConnection.release()
    }
}