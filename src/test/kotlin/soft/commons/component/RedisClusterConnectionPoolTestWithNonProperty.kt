package soft.commons.component

import kotlinx.coroutines.runBlocking
import soft.commons.config.RedisConfig
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("commons-test-none-redis")
@SpringBootTest(classes = [RedisConfig::class, RedisClusterConnectionPool::class])
class RedisClusterConnectionPoolTestWithNonProperty {

    @Autowired
    private lateinit var redisClusterConnectionPool: RedisClusterConnectionPool

    @Test
    fun test_dependencyInjection_notExistsRedisConnectionProperty_noCreated() {
        Assertions.assertNotNull(this.redisClusterConnectionPool)
        Assertions.assertFalse(this.redisClusterConnectionPool.created)
    }

    @Test
    fun test_acquire_notCreatedPool_throwNullPointException() {
        val hashTag = "{1:1}"

        val exception: NullPointerException? = Assertions.assertThrows(NullPointerException::class.java) {
            runBlocking {
                redisClusterConnectionPool.acquire(hashTag)
            }
        }

        Assertions.assertNotNull(exception)
    }
}