package soft.commons.config

import io.lettuce.core.RedisURI
import io.lettuce.core.SocketOptions
import io.lettuce.core.TimeoutOptions
import io.lettuce.core.cluster.ClusterClientOptions
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions
import io.lettuce.core.cluster.RedisClusterClient
import io.lettuce.core.resource.ClientResources
import io.lettuce.core.resource.DefaultClientResources
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component
import java.time.Duration

@Configuration
@Component
class RedisConfig(
    @Value("\${database.redis.hosts:}") private var hosts: String,
    @Value("\${database.redis.port:6379}") private var port: Int,
    @Value("\${database.redis.timeout:3}") private var timeout: Long
) {
    private var nodes: List<RedisURI> = if (this.hosts.isNotEmpty()) {
        hosts.split(",")
            .map { RedisURI.create(it, port) }
            .toList()
    } else {
        emptyList()
    }

    private var clientResources: ClientResources = DefaultClientResources.builder().build()
    private var clusterTopologyRefreshOptions: ClusterTopologyRefreshOptions = ClusterTopologyRefreshOptions.builder()
        .enablePeriodicRefresh(Duration.ofSeconds(10))
        .enableAdaptiveRefreshTrigger()
        .build()

    private fun createClusterClient(autoReconnect: Boolean = false): RedisClusterClient {
        val clusterClient = RedisClusterClient.create(this.clientResources, this.nodes);

        clusterClient.setOptions(
            ClusterClientOptions.builder()
                .autoReconnect(autoReconnect)
                .topologyRefreshOptions(this.clusterTopologyRefreshOptions)
                .socketOptions(SocketOptions.builder().connectTimeout(Duration.ofSeconds(this.timeout)).build())
                .timeoutOptions(TimeoutOptions.enabled(Duration.ofSeconds(this.timeout)))
                .build()
        );

        return clusterClient;
    }

    @Bean(destroyMethod = "shutdown")
    fun redisClusterClient(): RedisClusterClient? = if (hosts.isNotEmpty()) {
        createClusterClient(true)
    } else {
        null
    }

    @Bean(destroyMethod = "shutdown")
    fun redisClusterClientDisableAuthReconnect(): RedisClusterClient? = if (hosts.isNotEmpty()) {
        createClusterClient()
    } else {
        null
    }
}