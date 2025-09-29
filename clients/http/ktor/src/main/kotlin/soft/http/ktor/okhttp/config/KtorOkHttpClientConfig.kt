package soft.http.ktor.okhttp.config

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import soft.http.ktor.internal.CoroutineUtil
import soft.http.ktor.properties.KtorOkHttpClientProperties
import java.time.Duration
import java.util.concurrent.TimeUnit

@EnableConfigurationProperties(KtorOkHttpClientProperties::class)
@ConditionalOnProperty(prefix = "http.ktor", name = ["ok-http"], havingValue = "")
@Configuration
class KtorOkHttpClientConfig(
    private val ktorOkHttpClientProperties: KtorOkHttpClientProperties,
) {
    @Bean(destroyMethod = "close")
    fun okHttpClient(): HttpClient {
        return HttpClient(OkHttp) {
            engine {
                dispatcher = CoroutineUtil.vtDispatcher
                pipelining = ktorOkHttpClientProperties.pipelining
                preconfigured = makeOkHttpClient()
            }

            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        prettyPrint = true
                        ignoreUnknownKeys = true
                        explicitNulls = true
                    }
                )
            }

            defaultRequest {
                header(HttpHeaders.ContentType, ContentType.Application.Json.withCharset(Charsets.UTF_8))
            }
        }
    }


    fun makeOkHttpClient(): OkHttpClient {
        return with(ktorOkHttpClientProperties.httpProperties) {
            OkHttpClient.Builder()
                .connectTimeout(Duration.ofMillis(connectTimeoutMills))
                .readTimeout(Duration.ofMillis(readTimeoutMillis))
                .writeTimeout(Duration.ofMillis(writeTimeoutMillis))
                .pingInterval(pingIntervalMillis, TimeUnit.MILLISECONDS)
                .callTimeout(callTimeoutMillis, TimeUnit.MILLISECONDS)
                .protocols(protocols)
                .also { builder ->
                    if (okHttpDispatchers != null) {
                        builder.dispatcher(okhttp3.Dispatcher(makeDispatcher()).also { dispatcher ->
                            dispatcher.maxRequests = maxRequests
                            if (maxRequestPerHost != null) {
                                dispatcher.maxRequestsPerHost = maxRequestPerHost
                            }
                        })
                    }
                }
                .connectionPool(ConnectionPool(poolMaxIdleConnections, poolKeepAliveDurationSeconds, TimeUnit.SECONDS))
                .build()
        }
    }
}