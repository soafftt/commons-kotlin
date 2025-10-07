package soft.http.ktor.cio

import io.ktor.client.HttpClient
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@EnableConfigurationProperties(CioHttpProperties::class)
@ConditionalOnProperty(prefix = "http.ktor.cio", name = ["auto-configuration"], havingValue = "true")
@Configuration
class KtorCioHttpClientConfig(
    private val cioHttpProperties: CioHttpProperties,
) {
    @Bean
    fun ktorCioHttpClient(): HttpClient {
        return cioHttpProperties.makeHttpClient()
    }
}