package soft.http.ktor.cio.config

import io.ktor.client.*
import io.ktor.client.engine.cio.CIO
import io.ktor.client.engine.cio.endpoint
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import soft.http.ktor.internal.CoroutineUtil
import soft.http.ktor.properties.CioHttpProperties


@EnableConfigurationProperties(CioHttpProperties::class)
@ConditionalOnProperty(prefix = "http.ktor", name = ["cio"], havingValue = "")
@Configuration
class KtorCioHttpClientConfig(
    private val cioHttpProperties: CioHttpProperties,
) {
    @Bean(destroyMethod = "close")
    fun cioHttpClient(): HttpClient {
        return HttpClient(CIO) {
            engine {
                requestTimeout = cioHttpProperties.requestTimeout
                maxConnectionsCount = cioHttpProperties.maxConnectionsCount
                pipelining = cioHttpProperties.pipelining

                cioHttpProperties.cioDispatchers?.also { cioDispatcher ->
                    dispatcher = if (cioHttpProperties.cioDispatchers.useVtDispatchers) {
                        CoroutineUtil.vtDispatcher
                    } else {
                        CoroutineUtil.ioCoroutineDispatcher(cioHttpProperties.cioDispatchers.ioCount)
                    }
                }


                endpoint {
                    connectTimeout = cioHttpProperties.endpoint.connectTimeout

                    // 설정하지 않으면 알아서 처리.
                    if (cioHttpProperties.endpoint.maxConnectionsPerRoute != null) {
                        maxConnectionsPerRoute = cioHttpProperties.endpoint.maxConnectionsPerRoute
                    }

                    keepAliveTime = cioHttpProperties.endpoint.keepAliveTime
                    pipelineMaxSize = cioHttpProperties.endpoint.pipelineMaxSize
                    connectAttempts = cioHttpProperties.endpoint.connectAttempts
                    socketTimeout = cioHttpProperties.endpoint.socketTimeout
                }
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
}