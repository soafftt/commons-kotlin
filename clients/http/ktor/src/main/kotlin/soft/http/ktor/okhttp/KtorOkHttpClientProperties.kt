package soft.http.ktor.okhttp

import com.fasterxml.jackson.databind.ObjectMapper
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.http.*
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import okhttp3.Protocol
import org.springframework.boot.context.properties.ConfigurationProperties
import soft.http.ktor.internal.installContentsNavigation
import soft.http.ktor.internal.installDefaultRequest
import soft.http.ktor.properties.KtorDispatcherProperties
import java.time.Duration
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

@ConfigurationProperties(prefix = "http.ktor.ok-http")
data class KtorOkHttpClientProperties(
    private val autoConfiguration: Boolean = false,
    val pipelining: Boolean = false,
    /**
     * dispatchers 에서 thread 분리의 복적.
     *  * Dispatchers.IO 를 쓸수는 있으나, thread 공유 상태로 인하여 분리의 복적
     *  * useVtDispatcher = true 인 경우 무시됨
     */
    val dispatcher: KtorDispatcherProperties? = null,
    val okhttpClientProperties: OkHttpClientProperties = OkHttpClientProperties(),
)

fun KtorOkHttpClientProperties.makeOkHttpClient(
    objectMapper: ObjectMapper? = null,
    defaultUrl: String? = null,
    defaultMessageBuilder: ((builder: HttpMessageBuilder) -> Unit)? = null
): HttpClient {
    return this.let {
        HttpClient(OkHttp) {
            installDefaultRequest(
                defaultUrl,
                defaultMessageBuilder
            )

            engine {
                if(it.dispatcher != null) {
                    dispatcher = it.dispatcher.makeDispatcher()
                }

                pipelining = it.pipelining
                preconfigured = it.okhttpClientProperties.makeOkHttpClient()
            }

            installContentsNavigation(objectMapper)
        }
    }
}

data class OkHttpClientProperties(
    val connectionTimeoutMillis: Long = 1000,
    val readTimeoutMillis: Long = 1000,
    val writeTimeoutMillis: Long = 1000,
    val pingIntervalMillis: Long = 1000,
    val callTimeoutMillis: Long = 1000,
    val maxRequestsCount: Int = 500,
    val maxRequestsPerHost: Int? = null,
    val maxIdleConnectionsCount: Int = 250,
    val keepAliveDurationMillis: Long = 1000,
)

fun OkHttpClientProperties.makeOkHttpClient(): OkHttpClient {
    return OkHttpClient.Builder()
        .connectTimeout(Duration.ofMillis(this.connectionTimeoutMillis))
        .readTimeout(Duration.ofMillis(this.readTimeoutMillis))
        .writeTimeout(Duration.ofMillis(this.writeTimeoutMillis))
        .pingInterval(this.pingIntervalMillis, TimeUnit.MILLISECONDS)
        .callTimeout(this.callTimeoutMillis, TimeUnit.MILLISECONDS)
        .protocols(listOf(Protocol.HTTP_2, Protocol.HTTP_1_1))
        .dispatcher(okhttp3.Dispatcher(Executors.newVirtualThreadPerTaskExecutor()).also {
            it.maxRequests = this.maxIdleConnectionsCount

            if (this.maxRequestsPerHost != null)
                it.maxRequestsPerHost = this.maxRequestsPerHost

        })
        .connectionPool(ConnectionPool(this.maxIdleConnectionsCount, this.keepAliveDurationMillis, TimeUnit.MILLISECONDS))
        .build()
}