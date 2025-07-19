package soft.common.json

import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.time.LocalDate
import java.time.format.DateTimeFormatter

const val JSON_INITIALIZE = "{}"

val objectMapper : ObjectMapper by lazy(LazyThreadSafetyMode.PUBLICATION) {
    ObjectMapper(jsonFactory)
        .registerModules(
            KotlinModule.Builder().build(),
            JavaTimeModule().apply {
                addSerializer(
                    LocalDate::class.java,
                    LocalDateSerializer(
                        DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    )
                )
            }
        )

}

private val jsonFactory : JsonFactory by lazy(LazyThreadSafetyMode.PUBLICATION) { JsonFactory() }

fun <T> String.readJsonToObject(cls: Class<T>, ignoreException: Boolean = false): T? =
    runCatching {
        objectMapper.readValue(this, cls)
    }.getOrElse { throwable ->
        if (!ignoreException) {
            throw throwable
        }

        null
    }

fun String.readJsonToJsonNode(ignoreException: Boolean = false): JsonNode? =
    runCatching {
        objectMapper.readTree(this)
    }.getOrElse {
        if (!ignoreException) {
            throw it
        }

        null
    }


fun <T> JsonNode.toObject(cls: Class<T>, ignoreException: Boolean = true): T? =
    runCatching {
        objectMapper.treeToValue(this, cls)
    }.getOrElse {
        if (!ignoreException) {
            throw it
        }

        null
    }

fun <T> String.readJsonToList(cls: Class<T>, ignoreException: Boolean = false): List<T> =
    runCatching {
        objectMapper.readValue<List<T>>(this, objectMapper.typeFactory.constructCollectionType(List::class.java, cls))
    }.getOrElse {
        if (!ignoreException) {
            throw it
        }

        emptyList()
    }


fun Any.writeJson(ignoreException: Boolean = false): String =
    runCatching {
        objectMapper.writeValueAsString(this)
    }.getOrElse {
        if (!ignoreException) {
            throw it
        }

        JSON_INITIALIZE
    }