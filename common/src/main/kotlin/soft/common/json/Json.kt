package soft.common.json

import kotlinx.coroutines.CancellationException
import tools.jackson.core.json.JsonFactory
import tools.jackson.databind.JsonNode
import tools.jackson.databind.ObjectMapper
import tools.jackson.databind.json.JsonMapper
import tools.jackson.databind.module.SimpleModule
import tools.jackson.datatype.jsr310.ser.LocalDateSerializer
import tools.jackson.module.kotlin.KotlinModule
import java.time.LocalDate
import java.time.format.DateTimeFormatter

const val JSON_INITIALIZE = "{}"

val objectMapper: ObjectMapper by lazy(LazyThreadSafetyMode.PUBLICATION) {
    JsonMapper.builder(jsonFactory)
        .addModules(
            KotlinModule.Builder().build(),
            SimpleModule()
                .addSerializer(
                    LocalDate::class.java,
                    LocalDateSerializer(
                        DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    )
                )
        )
        .build()
}

private val jsonFactory: JsonFactory by lazy(LazyThreadSafetyMode.PUBLICATION) { JsonFactory() }

fun <T> String.readJsonToObject(cls: Class<T>, ignoreException: Boolean = false): T? =
    runCatching {
        objectMapper.readValue(this, cls)
    }.getOrElse { throwable ->
        if (throwable is CancellationException)
            throw throwable

        if (!ignoreException) {
            throw throwable
        }

        null
    }

fun String.readJsonToJsonNode(ignoreException: Boolean = false): JsonNode? =
    runCatching {
        objectMapper.readTree(this)
    }.getOrElse {
        if (it is CancellationException)
            throw it

        if (!ignoreException) {
            throw it
        }

        null
    }


fun <T> JsonNode.toObject(cls: Class<T>, ignoreException: Boolean = true): T? =
    runCatching {
        objectMapper.treeToValue(this, cls)
    }.getOrElse {
        if (it is CancellationException)
            throw it

        if (!ignoreException) {
            throw it
        }

        null
    }

fun <T> String.readJsonToList(cls: Class<T>, ignoreException: Boolean = false): List<T> =
    runCatching {
        objectMapper.readValue<List<T>>(this, objectMapper.typeFactory.constructCollectionType(List::class.java, cls))
    }.getOrElse {
        if (it is CancellationException)
            throw it

        if (!ignoreException) {
            throw it
        }

        emptyList()
    }


fun Any.writeJson(ignoreException: Boolean = false): String =
    runCatching {
        objectMapper.writeValueAsString(this)
    }.getOrElse {
        if (it is CancellationException)
            throw it

        if (!ignoreException) {
            throw it
        }

        JSON_INITIALIZE
    }