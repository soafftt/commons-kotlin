package soft.commons.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import reactor.netty.http.client.HttpClient
import reactor.netty.resources.ConnectionProvider
import java.time.Duration

@Configuration
class ReactHttpConnectorConfig {
    @Value("\${httpClient.maxConnections:200}")
    private var maxConnections: Int = 200;

    @Value("\${httpClient.maxIdleTime:8000}")
    private var maxIdleTime: Long = 8000;

    @Value("\${httpClient.maxLifeTime:8000}")
    private var maxLifeTime: Long = 8000;

    @Value("\${httpClient.pendingAcquireTimeout:0}")
    private var pendingAcquireTimeout: Long = 0;

    @Bean
    fun reactorClientHttpConnector(): ReactorClientHttpConnector {
        val provider: ConnectionProvider = ConnectionProvider.builder("webClient-connectProvider")
            .maxConnections(maxConnections) // maxConnections
            .pendingAcquireTimeout(Duration.ofMillis(pendingAcquireTimeout)) // connecitonPool에서 꺼내오는데의 timeout
            .pendingAcquireMaxCount(-1)
            .maxIdleTime(Duration.ofMillis(maxIdleTime)) // connectionPool에서의 idleTime
            .maxLifeTime(Duration.ofMillis(maxLifeTime)) // connecitonPool에서 connection이 유지되는 시간.
            .build();

        return ReactorClientHttpConnector(HttpClient.create(provider));
    }
}