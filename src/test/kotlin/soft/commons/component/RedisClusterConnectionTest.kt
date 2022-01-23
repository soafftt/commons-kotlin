package soft.commons.component

import soft.commons.config.RedisConfig
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles


@ActiveProfiles("commons-test")
@SpringBootTest(classes = [RedisConfig::class, RedisClusterConnection::class])
class RedisClusterConnectionTest {

    @Autowired
    private lateinit var redisClusterConnection: RedisClusterConnection

    @Test
    fun test_getConnection_success(): Unit {
        val connection = this.redisClusterConnection.redisConnection;

        Assertions.assertNotNull(connection)
        Assertions.assertNotNull(connection!!.isOpen)
    }
}