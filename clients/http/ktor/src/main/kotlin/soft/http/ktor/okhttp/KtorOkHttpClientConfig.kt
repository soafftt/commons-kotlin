package soft.http.ktor.okhttp

import com.fasterxml.jackson.databind.ObjectMapper
import io.ktor.client.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@EnableConfigurationProperties(KtorOkHttpClientProperties::class)
@ConditionalOnProperty(prefix = "http.ktor.ok-http", name = ["auto-configuration"], havingValue = "true")
@Configuration
class KtorOkHttpClientConfig(
    private val ktorOkHttpClientProperties: KtorOkHttpClientProperties
) {
    @Bean(destroyMethod = "close")
    fun httpClient(@Autowired(required = false) objectMapper: ObjectMapper? = null): HttpClient {
        return ktorOkHttpClientProperties.makeOkHttpClient(objectMapper)
    }
}