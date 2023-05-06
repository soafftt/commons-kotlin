package soft.commons.config

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy

@Lazy
@Configuration
class KtorClientConfig(
    @Value("\${ktor.client.engine.thread-count: 4}") private val engineThreadCount: Int,
    @Value("\${ktor.client.engine.max-connection-count: 10000}") private val engineMaxConnectionCount: Int,
    @Value("\${ktor.client.engine.request-timeout: 1000}") private val engineRequestTimeout: Long,
    @Value("\${ktor.client.endpoint.max-connections-per-route: 100}") private val endpointMaxConnectionsPerRoute: Int,
    @Value("\${ktor.client.endpoint.pipeline-max-size: 20}") private val endpointPipelineMaxSize: Int,
    @Value("\${ktor.client.endpoint.keep-alive-time: 1000}") private val endpointKeepAliveTime: Long,
    @Value("\${ktor.client.endpoint.connection-attempts: 5}") private val endpointConnectAttempts: Int,
    @Value("\${ktor.client.endpoint.connection-timeout: 500}") private val endpointConnectTimeout: Long
) {

    @Lazy
    @Bean
    fun cioHttpClient() = HttpClient(CIO) {
        install(ContentNegotiation) {
            jackson()
        }

        Charsets {
            // allow using UTF-8
            register(Charsets.UTF_8)

            // Specify Charset to send request(if no charset in request headers).
            sendCharset = Charsets.UTF_8

            // Specify Charset to receive response(if no charset in response headers).
            responseCharsetFallback = Charsets.UTF_8
        }

        engine {
            threadsCount = engineThreadCount
            maxConnectionsCount = engineMaxConnectionCount
            requestTimeout = engineRequestTimeout

            endpoint {
                maxConnectionsPerRoute = endpointMaxConnectionsPerRoute
                pipelineMaxSize = endpointPipelineMaxSize
                keepAliveTime = endpointKeepAliveTime
                connectAttempts = endpointConnectAttempts
                connectTimeout = endpointConnectTimeout
            }
        }

        defaultRequest {
            contentType(ContentType.Application.Json)
        }

    }
}