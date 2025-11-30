package soft.valkey

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import soft.soft.valkey.command.ValkeyCommands
import soft.soft.valkey.config.GlideStandAloneRegister

@SpringBootTest(
    classes = [GlideStandAloneRegister::class, ValKeyConfig::class],
)
class ValkeyBeanLoadTest {
    @Autowired
    lateinit var valkeyCommands: ValkeyCommands

    @Test
    fun test() {
        val ddd = ""
    }
}