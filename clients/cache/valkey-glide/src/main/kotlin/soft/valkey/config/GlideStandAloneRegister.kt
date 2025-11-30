package soft.soft.valkey.config

import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.BeanRegistry
import org.springframework.core.env.Environment
import soft.soft.valkey.DefaultMessageSubscription
import soft.soft.valkey.command.ValKeyPubSubCommand
import soft.soft.valkey.command.ValkeyCommands
import soft.soft.valkey.properties.MultiStandAloneProperties
import soft.soft.valkey.properties.StandAlonePubSubProperties
import soft.soft.valkey.properties.StrandAloneProperties

/**
 * 아래와 같이 Import 하여 사용가능
 *
 * @Configuration
 * @Import(ValkeyClientRegister::class)
 * class ValkeyClientConfig
 */
class GlideStandAloneRegister: AbstractBeanRegistrar() {
    companion object {
        private const val PROPERTY_KEY = "valkey.standalone.single"
        private const val BEAN_NAME = "valkeyCommands"
    }
    /**
     * Spring 의 @Import 를 이용하여 Bean 을 구성하도록 합니다.
     */
    override fun register(registry: BeanRegistry, env: Environment) {
        val properties = bindProperties<StrandAloneProperties>(PROPERTY_KEY, env)
        registry.registerBean(BEAN_NAME, ValkeyCommands::class.java) { spec ->
            spec.supplier { context ->
                runBlocking {
                    ValkeyCommands.from(properties.toGlideClient())
                }
            }
        }
    }
}

class GlideStandAloneMultiRegister : AbstractBeanRegistrar() {
    companion object {
        private const val PROPERTY_KEY = "valkey.standalone"
        private const val BEAN_NAME_POSTFIX = "ValkeyCommands"
    }

    override fun register(registry: BeanRegistry, env: Environment) {
        val properties = bindProperties<MultiStandAloneProperties>(PROPERTY_KEY, env)
        properties.multi.filter { it.key != "pubsub" && it.key != "single" }.forEach { (key, value) ->
            registry.registerBean("${key}${BEAN_NAME_POSTFIX}", ValkeyCommands::class.java) { spec ->
                spec.supplier {
                    runBlocking {
                        ValkeyCommands.from(value.toGlideClient())
                    }
                }
            }
        }
    }
}

class GlideStandAlonePubSubRegistrar : AbstractBeanRegistrar() {
    companion object {
        private const val PROPERTY_KEY = "valkey.standalone.pubsub"
    }

    override fun register(registry: BeanRegistry, env: Environment) {
        val properties = bindProperties<StandAlonePubSubProperties>(PROPERTY_KEY, env)
            .apply {
                registerMessageCallback(DefaultMessageSubscription())
            }

        registry.registerBean("valkeyPubSub", ValKeyPubSubCommand::class.java) { spec ->
            spec.supplier { context ->
                runBlocking {
                    ValKeyPubSubCommand.from(properties.toGlideClient(), properties.messageSubscription)
                }
            }
        }
    }
}




