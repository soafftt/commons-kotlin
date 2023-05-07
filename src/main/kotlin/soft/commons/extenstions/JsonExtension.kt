package soft.commons.extenstions

import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import soft.commons.enums.CommonsErrorCode
import soft.commons.exception.CommonsRuntimeException
import java.lang.Exception

class JsonExtension {
    companion object {
        const val JSON_INIT_STRING = "{}"

        private val log: Logger = LoggerFactory.getLogger(JsonExtension::class.java)

        private val jsonFactory: JsonFactory = JsonFactory()
        private val objectMapper: ObjectMapper = ObjectMapper(jsonFactory)

        fun <T> String.deserializeJson(cls: Class<T>, ignoreException: Boolean = true): T? =
            runCatching {
                objectMapper.readValue(this, cls)
            }.getOrElse { throwable ->
                log.error("==> String.deserializeJson $this", throwable)

                when(ignoreException) {
                    true -> null
                    else -> throw CommonsRuntimeException(CommonsErrorCode.JSON_DESERIALIZE_ERROR, throwable)
                }
            }

        fun <T> JsonNode.deserializeJson(cls: Class<T>, ignoreException: Boolean = true): T? =
            try {
                objectMapper.treeToValue(this, cls)
            } catch(ex: Exception) {
                when(ignoreException) {
                    true -> null
                    else -> throw CommonsRuntimeException(CommonsErrorCode.JSON_DESERIALIZE_ERROR, ex)
                }
            }

        fun <T> String.deserializeJsonArray(cls: Class<T>, ignoreException: Boolean = true): List<T> =
            try {
                objectMapper.readValue(
                    this,
                    objectMapper.typeFactory.constructCollectionType(
                        List::class.java,
                        cls
                    )
                )
            } catch(ex: Exception) {
                when(ignoreException) {
                    true -> emptyList<T>()
                    else -> throw CommonsRuntimeException(CommonsErrorCode.JSON_DESERIALIZE_ERROR, ex)
                }
            }

        fun Any.toJson(ignoreException: Boolean = false): String =
            runCatching {
                objectMapper.writeValueAsString(this)
            }.getOrElse {
                log.error("==> Any.toJson serialize", it)

                when(ignoreException) {
                    true -> JSON_INIT_STRING
                    else -> throw CommonsRuntimeException(CommonsErrorCode.JSON_SERIALIZE_ERROR, it)
                }
            }
    }
}