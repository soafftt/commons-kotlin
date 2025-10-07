package soft.http.ktor.properties

import okhttp3.Protocol
import org.springframework.boot.context.properties.ConfigurationProperties
import soft.http.ktor.internal.CoroutineUtil
import java.util.concurrent.ExecutorService

@ConfigurationProperties(prefix = "http.ktor.ok-http")
data class KtorOkHttpClientProperties(
    val pipelining: Boolean = false,
    val httpProperties: OkHttpProperties = OkHttpProperties()
)

data class OkHttpProperties(
    val connectTimeoutMills: Long = 15_000,
    val readTimeoutMillis: Long = 1000,
    val writeTimeoutMillis: Long = 1000,
    val pingIntervalMillis: Long = 10000,
    val callTimeoutMillis: Long = 10000,
    val protocols: List<Protocol> = listOf(Protocol.HTTP_2, Protocol.HTTP_1_1),
    val poolMaxIdleConnections: Int = 5,
    val poolKeepAliveDurationSeconds: Long = 10,
    val maxRequests: Int = 500,
    val maxRequestPerHost: Int? = null,
    val okHttpDispatchers: KtorDispatcherProperties? = null
) {
    fun makeDispatcher() : ExecutorService {
        if (okHttpDispatchers == null)
            throw IllegalArgumentException("You must use either ioCount or vtDispatchers.")

        return if (okHttpDispatchers.ioCount > 0) {
            CoroutineUtil.fixedThreadPool(okHttpDispatchers.ioCount)
        } else {
            CoroutineUtil.vtThread
        }
    }
}