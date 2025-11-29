package soft.soft.valkey.config

import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.BeanRegistrar
import org.springframework.beans.factory.BeanRegistry
import org.springframework.boot.context.properties.bind.Binder
import org.springframework.core.env.Environment
import soft.soft.valkey.ValkeyCommands
import soft.soft.valkey.properties.StrandAloneProperties

/**
 * 아래와 같이 Import 하여 사용가능
 *
 * @Configuration
 * @Import(ValkeyClientRegister::class)
 * class ValkeyClientConfig
 */
class ValkeyClientRegister : BeanRegistrar {
    /**
     * Spring 의 @Import 를 이용하여 Bean 을 구성하도록 합니다.
     */
    override fun register(registry: BeanRegistry, env: Environment) {
        val binder = Binder.get(env)
        val standAloneProperties = binder.bind("valkey.standalone", StrandAloneProperties::class.java)
            .orElse(null)

        if (standAloneProperties != null) {
            runBlocking {
                registry.registerBean("valkeyCommands", ValkeyCommands::class.java) { spec ->
                    spec.supplier { context ->
                        runBlocking {
                            ValkeyCommands.from(standAloneProperties.toGlideClient())
                        }
                    }
                }
            }
        }
    }
}




