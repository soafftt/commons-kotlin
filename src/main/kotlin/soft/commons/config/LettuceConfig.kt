package soft.commons.config

import io.lettuce.core.RedisURI
import io.lettuce.core.SocketOptions
import io.lettuce.core.TimeoutOptions
import io.lettuce.core.cluster.ClusterClientOptions
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions
import io.lettuce.core.cluster.RedisClusterClient
import io.lettuce.core.cluster.api.reactive.RedisClusterReactiveCommands
import io.lettuce.core.cluster.api.sync.RedisClusterCommands
import io.lettuce.core.codec.StringCodec
import io.lettuce.core.resource.DefaultClientResources
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy
import java.time.Duration

@Lazy
@Configuration
class LettuceConfig(
    @Value("\${redis.hosts:}") private val hosts: String,
    @Value("\${redis.password: }") private val password: CharSequence,
    @Value("\${redis.ssl: false}") private val withSsl: Boolean,
    @Value("\${redis.timeout: 1}") private val timeout: Long
) {
    private val topologyRefreshSeconds: Long = 10L

    @Lazy
    @Bean(destroyMethod = "shutdown")
    fun redisClusterClient(): RedisClusterClient =
        RedisClusterClient.create(
            DefaultClientResources.builder().build(),
            hosts.split(",").map { it ->
                val hostPort = it.split(":");
                RedisURI.builder().also { builder ->
                    builder.withSsl(this.withSsl)
                    builder.withHost(hostPort[0])

                    if (hostPort.size == 2) {
                        builder.withPort(hostPort[1].toInt())
                    }

                    if (password.isNotEmpty()) {
                        builder.withPassword(password)
                    }
                }.build()
            }.toList()
        ).also {
            it.setOptions(
                ClusterClientOptions.builder()
                    .autoReconnect(true)
                    .topologyRefreshOptions(
                        ClusterTopologyRefreshOptions.builder()
                            .enablePeriodicRefresh(Duration.ofSeconds(topologyRefreshSeconds))
                            .enableAdaptiveRefreshTrigger()
                            .build()
                    )
                    .socketOptions(SocketOptions.builder().connectTimeout(Duration.ofSeconds(this.timeout)).build())
                    .timeoutOptions(TimeoutOptions.enabled(Duration.ofSeconds(this.timeout)))
                    .build()
            )
        }


    @Lazy
    @Bean
    fun redisClusterSyncCommands(
        redisClusterClient: RedisClusterClient
    ): RedisClusterCommands<String, String> =
        redisClusterClient.connect(StringCodec.UTF8).sync()

    @Lazy
    @Bean
    fun redisClusterReactiveCommands(
        redisClusterClient: RedisClusterClient
    ): RedisClusterReactiveCommands<String, String> =
        redisClusterClient.connect(StringCodec.UTF8).reactive()
}