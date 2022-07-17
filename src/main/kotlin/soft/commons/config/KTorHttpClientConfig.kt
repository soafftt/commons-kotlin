package soft.commons.config

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Value

@Component
@Configuration
class KTorHttpClientConfig(
    @Value("\${ktor.client.engine.threadCount:4}") val engineThreadCount: Int,
    @Value("\${ktor.client.engine.maxConnectionsCount:10000}") val engineMaxConnectionsCount: Int,
    @Value("\${ktor.client.engine.requestTimeout:1000}") val engineRequestTimeout: Long,
    @Value("\${ktor.client.endpoint.maxConnectionsPerRoute:100}") val endPointMaxConnectionPerRoute: Int,
    @Value("\${ktor.client.endpoint.pipelineMaxSize:20}") val endPointPipeLineMaxSize: Int,
    @Value("\${ktor.client.endpoint.keepAliveTime:5000}") val endPointKeepAliveTime: Long,
    @Value("\${ktor.client.endpoint.connectTimeout:1000}") val endPointConnectionTimeout: Long,
    @Value("\${ktor.client.endpoint.connectAttempts:5}") val endPointConnectionAttempts: Int,
) {
    @Bean
    fun httpClient(): HttpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            Json {
                ignoreUnknownKeys = true
                isLenient = true
            }
        }
        engine {
            threadsCount = engineThreadCount
            maxConnectionsCount = engineMaxConnectionsCount
            requestTimeout = engineRequestTimeout
            endpoint {
                maxConnectionsPerRoute = endPointMaxConnectionPerRoute
                pipelineMaxSize = endPointPipeLineMaxSize
                keepAliveTime = endPointKeepAliveTime
                connectTimeout = endPointConnectionTimeout
                connectAttempts = endPointConnectionAttempts
            }
        }
        defaultRequest {
            contentType(ContentType.Application.Json)
        }
    }
}