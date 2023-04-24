package soft.commons.config

import io.lettuce.core.cluster.RedisClusterClient
import io.lettuce.core.cluster.api.reactive.RedisClusterReactiveCommands
import io.lettuce.core.cluster.api.sync.RedisClusterCommands
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import kotlin.math.exp

@SpringBootTest
@ActiveProfiles("commons-test")
class LettuceConfigTest {

    @Autowired
    private lateinit var redisClusterClient: RedisClusterClient

    @Autowired
    private lateinit var redisClusterSyncCommands: RedisClusterCommands<String, String>

    @Autowired
    private lateinit var redisClusterReactiveCommands: RedisClusterReactiveCommands<String, String>

    @Test
    @DisplayName("redisClusterClient inject")
    fun `redisClusterClient inject success`() {
        Assertions.assertNotNull(this.redisClusterClient)
    }

    @Test
    @DisplayName("redisClusterSyncCommands inject")
    fun `redisClusterClient#redisClusterSyncCommands inject success`() {
        Assertions.assertNotNull(this.redisClusterSyncCommands)
    }

    @Test
    @DisplayName("redisClusterSyncCommands inject with get/set commands")
    fun `redisClusterClient#redisClusterSyncCommands inject with get_set success`() {
        val expectedString = "A"
        val redisKey = "t:get"

        this.redisClusterSyncCommands.set(redisKey, expectedString)

        val assertGet = this.redisClusterSyncCommands.get(redisKey)

        Assertions.assertEquals(expectedString, assertGet)
    }

    @Test
    @DisplayName("redisClusterReactiveCommands inject")
    fun `redisClusterClient#redisClusterReactiveCommands inject success`() {
        Assertions.assertNotNull(this.redisClusterReactiveCommands)
    }

    @Test
    @DisplayName("redisClusterReactiveCommands inject get/set commands")
    fun `redisClusterClient#redisClusterReactiveCommands inject with get_set success`() {
        val expectedString = "A"
        val redisKey = "t:get:reactive"

        Assertions.assertDoesNotThrow() {
            runBlocking {
                redisClusterReactiveCommands.set(redisKey, expectedString).awaitSingle()
            }
        }

        Assertions.assertDoesNotThrow() {
            val assertGet = runBlocking {
                redisClusterReactiveCommands.get(redisKey).awaitSingle()
            }

            Assertions.assertEquals(expectedString, assertGet)
        }

    }

}