package soft.http.ktor

import com.fasterxml.jackson.databind.ObjectMapper
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMessageBuilder
import io.ktor.http.withCharset
import io.ktor.serialization.jackson.JacksonConverter
import io.ktor.utils.io.charsets.name
import kotlinx.serialization.json.Json
import java.nio.charset.StandardCharsets

internal fun HttpClientConfig<*>.installContentsNavigation(objectMapper: ObjectMapper? = null) {
    install(ContentNegotiation) {
        objectMapper?.also {
            this.register(ContentType.Application.Json, JacksonConverter(objectMapper))
        } ?: Json {
            ignoreUnknownKeys = true
        }
    }
}

internal fun HttpClientConfig<*>.installDefaultRequest(
    defaultUrl: String? = null,
    defaultMessageBuilder: ((builder: HttpMessageBuilder) -> Unit)? = null
) {
    defaultRequest {
        if (!defaultUrl.isNullOrBlank()) {
            url(defaultUrl)
        }

        header(HttpHeaders.ContentType, ContentType.Application.Json.withCharset(StandardCharsets.UTF_8))
        header(HttpHeaders.AcceptEncoding, StandardCharsets.UTF_8.name)

        defaultMessageBuilder?.invoke(this)
    }
}