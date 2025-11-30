package soft.soft.valkey.properties

import glide.api.GlideClient
import glide.api.models.GlideString
import glide.api.models.configuration.AdvancedGlideClientConfiguration
import glide.api.models.configuration.GlideClientConfiguration
import glide.api.models.configuration.ProtocolVersion
import glide.api.models.configuration.ReadFrom
import glide.api.models.configuration.StandaloneSubscriptionConfiguration
import kotlinx.coroutines.future.await
import org.springframework.boot.context.properties.bind.ConstructorBinding
import soft.soft.valkey.ValkeyMessageSubscription
import javax.naming.ConfigurationException

interface IStandAloneProperties {
    val connectionTimeoutMills: Int
    val credentials: Credential?
    val useSsl: Boolean
    val protocol: ProtocolVersion
    val reconnectStrategy: ConnectionRetryStrategy?
    val clientAZ: String?
    val clientName: String?
    val inflightRequestsLimit: Int?
    val lazyConnection: Boolean
    val requestTimeoutMills: Int
    val dataBaseId: Int?

    fun toGlideConfiguration(): GlideClientConfiguration

    suspend fun toGlideClient(): GlideClient {
        return GlideClient.createClient(toGlideConfiguration()).await()
    }

    fun makeGlideConfigurationBuilder(): GlideClientConfiguration.GlideClientConfigurationBuilder<*, *> {
        return with(GlideClientConfiguration.builder()) {
            this.useTLS(useSsl)
                .protocol(protocol)
                .lazyConnect(lazyConnection)
                .requestTimeout(requestTimeoutMills)
                .advancedConfiguration(
                    AdvancedGlideClientConfiguration.builder()
                        .connectionTimeout(connectionTimeoutMills)
                        .build()
                )

            credentials?.also { this.credentials(it.toServerCredentials()) }
            reconnectStrategy?.also { this.reconnectStrategy(it.toBackoffStrategy()) }
            clientAZ?.also { this.clientAZ(it) }
            clientName?.also { this.clientName(it) }
            inflightRequestsLimit?.also { this.inflightRequestsLimit(it) }
            dataBaseId?.also { this.databaseId(it) }

            return@with this
        }
    }
}




data class StrandAloneProperties @ConstructorBinding constructor(
    val address: Address,
    override val connectionTimeoutMills: Int = 1000,
    override val credentials: Credential? = null,
    override val useSsl: Boolean = false,
    override val protocol: ProtocolVersion = ProtocolVersion.RESP3,
    override val reconnectStrategy: ConnectionRetryStrategy? = null,
    override val clientAZ: String? = null,
    override val clientName: String? = null,
    override val inflightRequestsLimit: Int? = null,
    override val lazyConnection: Boolean = false,
    override val requestTimeoutMills: Int = 1000,
    override val dataBaseId: Int? = null
) : IStandAloneProperties {

    override fun toGlideConfiguration(): GlideClientConfiguration {
        return makeGlideConfigurationBuilder()
            .address(address.toNodeAddress())
            .build()
    }
}

data class MultiStandAloneProperties(
    val multi: Map<String, StrandAloneProperties>
)

/**
 * PubSub 은 클라이언트 분리가 유리
 * 지족석은 Subscribe 대기로 인하여 일반 병령어의 지연이 발생할 수 있습니다. (Redis / Valkey 의 동일한 권장사항)
 * ref: https://github.com/valkey-io/valkey-glide/wiki/General-Concepts#pubsub-support
 * Best Practice: Due to the implementation of the resubscription logic, it is recommended to use a dedicated client for PubSub subscriptions.
 * That is, the client with subscriptions should not be the same client that issues commands.
 * In case of topology changes, the internal connections might be reestablished to resubscribe to the correct servers.
 */
data class StandAlonePubSubProperties(
    val address: Address,
    override val connectionTimeoutMills: Int,
    override val credentials: Credential? = null,
    override val useSsl: Boolean,
    override val protocol: ProtocolVersion = ProtocolVersion.RESP3,
    override val reconnectStrategy: ConnectionRetryStrategy? = null,
    override val clientAZ: String? = null,
    override val clientName: String? = null,
    override val inflightRequestsLimit: Int? = null,
    override val lazyConnection: Boolean = false,
    override val requestTimeoutMills: Int = 1000,
    override val dataBaseId: Int? = null,
    val subscribeKeys: List<String>
) : IStandAloneProperties {
    private lateinit var subscriptionCallBack: ValkeyMessageSubscription

    val messageSubscription: ValkeyMessageSubscription
        get() = subscriptionCallBack

    fun registerMessageCallback(callBack: ValkeyMessageSubscription) {
        subscriptionCallBack = callBack
    }

    override fun toGlideConfiguration(): GlideClientConfiguration {
        if (!::subscriptionCallBack.isInitialized) {
            throw ConfigurationException("Subscribe callback is not set")
        }

        return makeGlideConfigurationBuilder()
            .address(address.toNodeAddress())
            .subscriptionConfiguration(
                StandaloneSubscriptionConfiguration.builder()
                    .subscriptions(
                        StandaloneSubscriptionConfiguration.PubSubChannelMode.EXACT,
                        subscribeKeys.map { GlideString.of(it) }.toSet()
                    )
                    .callback(subscriptionCallBack::messageCallback)
                    .build()
            )
            .build()
    }
}

data class MasterReplicaProperties(
    val addresses: List<Address>,
    override val connectionTimeoutMills: Int,
    override val credentials: Credential? = null,
    override val useSsl: Boolean,
    override val protocol: ProtocolVersion = ProtocolVersion.RESP3,
    override val reconnectStrategy: ConnectionRetryStrategy? = null,
    override val clientAZ: String? = null,
    override val clientName: String? = null,
    override val inflightRequestsLimit: Int? = null,
    override val lazyConnection: Boolean = false,
    override val requestTimeoutMills: Int = 1000,
    override val dataBaseId: Int? = null,
    val readFrom: ReadFrom = ReadFrom.PREFER_REPLICA
) : IStandAloneProperties {

    override fun toGlideConfiguration(): GlideClientConfiguration {
        return makeGlideConfigurationBuilder()
            .addresses(addresses.map { it.toNodeAddress() })
            .build()
    }
}

