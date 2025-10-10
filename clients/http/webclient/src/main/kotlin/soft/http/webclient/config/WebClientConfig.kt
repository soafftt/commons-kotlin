package soft.http.webclient.config

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@EnableConfigurationProperties(WebClientProperties::class)
@ConditionalOnProperty(prefix = "http.spring.webclient", name = ["auto-configuration"], havingValue = "true")
@Configuration
class WebClientConfig(
    private val webClientProperties: WebClientProperties,
) {
    @Bean
    fun webClient(): WebClient {
        return webClientProperties.makeWebClient()
    }
}