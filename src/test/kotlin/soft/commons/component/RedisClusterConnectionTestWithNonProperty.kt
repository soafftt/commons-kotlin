package soft.commons.component

import soft.commons.config.RedisConfig
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("commons-test-none-redis")
@SpringBootTest(classes = [RedisConfig::class, RedisClusterConnection::class])
class RedisClusterConnectionTestWithNonProperty {

    @Autowired
    lateinit var redisClusterConnection: RedisClusterConnection

    @Test
    fun test_dependencyInjection_whenNotExistsRedisProperty_noCreated() {
        Assertions.assertNotNull(this.redisClusterConnection)
        Assertions.assertFalse(this.redisClusterConnection.created)
    }

    @Test
    fun test_dependencyInjection_whenNotExistsRedisProperty_throwNullPointException() {
        Assertions.assertThrows(NullPointerException::class.java) {
            redisClusterConnection.redisConnection?.isOpen ?: throw NullPointerException("error")
        }
    }

}