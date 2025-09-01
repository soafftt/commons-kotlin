package soft.http.ktor.cio.config

import io.ktor.client.*
import io.ktor.client.engine.cio.CIO
import io.ktor.client.engine.cio.endpoint
import kotlinx.coroutines.Dispatchers
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import soft.http.ktor.cio.CIOHttpProperties


@EnableConfigurationProperties(CIOHttpProperties::class)
@ConditionalOnProperty(prefix = "http.ktor", name = ["cio"], havingValue = "true")
@Configuration
class CIOHttpClientConfig(
    private val cioHttpProperties: CIOHttpProperties,
) {
    @Bean
    fun cioHttpClient(): HttpClient {
        return HttpClient(CIO) {

            engine {
                requestTimeout = cioHttpProperties.requestTimeout
                maxConnectionsCount = cioHttpProperties.maxConnectionsCount
                pipelining = cioHttpProperties.pipelining
                dispatcher = Dispatchers.IO
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
        }
    }

}