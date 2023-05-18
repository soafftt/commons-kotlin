package soft.commons.util

import net.logstash.logback.marker.Markers
import org.slf4j.Logger
import org.slf4j.Marker;
import soft.commons.extenstions.StringExtension.Companion.formatUsageMessageFormat
import java.util.*
import kotlin.collections.LinkedHashMap

class LogUtil {
    companion object {
        fun debug(logger: Logger, pattern: String, vararg args: Any) {
            logger.debug(makeMarker(), pattern.formatUsageMessageFormat(args))
        }

        fun debug(logger: Logger, message: String) {
            logger.debug(makeMarker(), message)
        }

        fun debug(logger: Logger, uuid: UUID, pattern: String, vararg args: Any) {
            logger.debug(makeMarker(uuid = uuid), pattern.formatUsageMessageFormat(args))
        }

        fun debug(logger: Logger, throwable: Throwable, message: String? = null, uuid: UUID? = null) {
            val logMessage = makeMessage(throwable, message)
            logger.debug(makeMarker(logMessage, uuid, throwable = throwable), throwable.message)
        }

        fun info(logger: Logger, pattern: String, vararg args: Any) {
            logger.info(makeMarker(), pattern.formatUsageMessageFormat(args))
        }

        fun info(logger: Logger, message: String, block: (() -> Map<String, Any>)? = null) {
            val markerMap = makeMarkerMap(message = message)
            if (block != null) {
                block().forEach() {
                    markerMap[it.key] = it.value
                }
            }

            logger.info(Markers.appendEntries(markerMap), message)
        }

        fun error(logger: Logger, pattern: String, vararg args: Any) {
            logger.error(makeMarker(), pattern.format(args))
        }

        fun error(logger: Logger, message: String, block: (() -> Map<String, Any>)? = null) {
            val markerMap = makeMarkerMap(message = message)
            if (block != null) {
                block().forEach() {
                    markerMap[it.key] = it.value
                }
            }

            logger.error(Markers.appendEntries(markerMap), message)
        }

        fun error(logger: Logger, uuid: UUID, pattern: String, vararg args: Any) {
            logger.error(makeMarker(uuid = uuid), pattern.formatUsageMessageFormat(args))
        }

        fun error(logger: Logger, throwable: Throwable, message: String? = null, uuid: UUID? = null, block: (() -> Map<String, Any>)? = null) {
            val logMessage = makeMessage(throwable, message)
            val markerMap = makeMarkerMap(message = logMessage, uuid, throwable = throwable)
            if (block != null) {
                block().forEach() {
                    markerMap[it.key] = it.value
                }
            }

            logger.error(Markers.appendEntries(markerMap), message)
        }

        private fun makeMessage(throwable: Throwable, message: String?) =
            (message ?: throwable.message ?: "")

        private fun makeMarker(message: String? = null, uuid: UUID? = null, throwable: Throwable? = null): Marker =
            makeMarkerMap(message, uuid, throwable).let { Markers.appendEntries(it) }

        private fun makeMarkerMap(message: String? = null, uuid: UUID? = null, throwable: Throwable? = null): LinkedHashMap<String, Any> =
            linkedMapOf<String, Any>(
                Pair("uuid", uuid?.toString() ?: UUID.randomUUID().toString())
            ).also {
                if (throwable != null) {
                    it["stackTrace"] = throwable.stackTrace
                }

                if (message.isNullOrEmpty()) {
                    it["message"] = message ?: ""
                }
            }
    }
}