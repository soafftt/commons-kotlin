package soft.http.webclient.config

import io.netty.channel.ChannelOption
import io.netty.handler.timeout.ReadTimeoutHandler
import io.netty.handler.timeout.WriteTimeoutHandler
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import reactor.netty.http.client.HttpClient
import reactor.netty.resources.ConnectionProvider
import java.time.Duration
import java.util.concurrent.TimeUnit


@ConfigurationProperties(prefix = "http.spring.webclient")
data class WebClientProperties(
    private val autoConfiguration: Boolean = false,
    val pooling: WebClientPoolProperties = WebClientPoolProperties(),
    val keepAlive: Boolean = true,
    val connectionTimeoutMills: Int = 1000,
    val responseTimeoutMills: Long = 5000,
    val readTimeoutMillis: Long = 2000,
    val writeTimeoutMillis: Long = 3000,
    val compress: Boolean = false
) {
    fun makeWebClient(
        poolName: String = "webclient-pool",
        baseUrl: String? = null
    ): WebClient {
        val client = HttpClient.create(pooling.makeConnectionProvider(poolName))
            .keepAlive(keepAlive)
            // 자동으로 gzip 압축
            // cpu 는 좀더 사용하지만, 대량 데이터 처리에 유리.
            .compress(compress)
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectionTimeoutMills)
            .responseTimeout(Duration.ofMillis(responseTimeoutMills))
            .doOnConnected { conn ->
                conn.addHandlerLast(ReadTimeoutHandler(readTimeoutMillis, TimeUnit.MILLISECONDS))
                    .addHandlerLast(WriteTimeoutHandler(writeTimeoutMillis, TimeUnit.MILLISECONDS))
            }
            .headers { headers ->
                headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            }

        return WebClient.builder()
            .clientConnector(ReactorClientHttpConnector(client))
            .also { builder ->
                if (!baseUrl.isNullOrBlank()) {
                    builder.baseUrl(baseUrl)
                }
            }
            .filter { request, next ->
                next.exchange(request)
                    .flatMap { response ->
                        // 💡 여기서 response.bodyToFlux()나 bodyToMono()를 건드리지 않음!
                        Mono.just(response)
                    }
                    // 안전하게 응답 이후 체인 이동 (pool 반환 스케줄러 분리)
                    .publishOn(Schedulers.boundedElastic())
            }
            .build()
    }
}


data class WebClientPoolProperties(
    val maxConnections: Int = 100,
    val pendingAcquireMaxCount: Int = 50,
    val pendingAcquireTimeoutMillis: Long = 1000,
    val maxIdleTimeSeconds: Long = 150,
    val maxLifeTimeSeconds: Long = 300,
    val evictInBackgroundMills: Long = 120
) {
    internal fun makeConnectionProvider(poolName: String) : ConnectionProvider {
        return ConnectionProvider.builder(poolName)
            // 최대 커넥션 수
            .maxConnections(this.maxConnections)
            // 커넥션이 부족할 때 대기 가능한 요청 수
            .pendingAcquireMaxCount(this.pendingAcquireMaxCount)
            // 대기 타임아웃
            .pendingAcquireTimeout(Duration.ofMillis(pendingAcquireTimeoutMillis))
            // 커넥션 유휴 시간
            .maxIdleTime(Duration.ofSeconds(maxIdleTimeSeconds))
            // 커넥션 수명
            .maxLifeTime(Duration.ofSeconds(maxLifeTimeSeconds))
            // pooling 에서 Connection 정리 대기 시간.
            .evictInBackground(Duration.ofMillis(evictInBackgroundMills))
            .build()
    }
}