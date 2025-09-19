package soft.http.ktor.okhttp

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import kotlinx.coroutines.Dispatchers
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import okhttp3.Protocol
import org.springframework.context.annotation.Bean
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class KtorOkHttpClientConfig {
    @Bean
    fun ktorOkHttpClient(okHttpClient: OkHttpClient): HttpClient {
        return HttpClient(OkHttp) {
            engine {
                dispatcher = Dispatchers.Unconfined
                pipelining = true
                preconfigured = okHttpClient
            }
        }
    }

    @Bean("okHttpClient")
    fun http(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(java.time.Duration.ofMillis(1000))
            .readTimeout(java.time.Duration.ofMillis(1000))
            .writeTimeout(java.time.Duration.ofMillis(1000))
            .pingInterval(10, TimeUnit.SECONDS)
            .callTimeout(10, TimeUnit.SECONDS)
            .protocols(listOf(Protocol.HTTP_2, Protocol.HTTP_1_1))
            .dispatcher(okhttp3.Dispatcher(Executors.newFixedThreadPool(4)).also {
                it.maxRequests = 10
                it.maxRequestsPerHost = 10
                it.idleCallback
            })
            .connectionPool(ConnectionPool(5, 10, TimeUnit.SECONDS))
            .build()
    }
}