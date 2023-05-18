package soft.commons.extenstions

import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.text.MessageFormat
import java.util.*

class StringExtension {
    companion object {
        private val CHARSET_UTF8: Charset = StandardCharsets.UTF_8;
        private val base64Encoder = Base64.getMimeEncoder()
        private val base64Decoder = Base64.getMimeDecoder()

        fun String.toBase64String(): String =
            base64Encoder.encodeToString(toByteArray())

        fun String.toBase64Array(): ByteArray =
            base64Decoder.decode(this)

        fun ByteArray.toBase64Array(): ByteArray =
            base64Encoder.encode(this)

        fun ByteArray.toBase64String(): String =
            base64Encoder.encodeToString(this)

        fun String.toHexArray(): ByteArray =
            when (this.isEmpty()) {
                true -> ByteArray(0)
                else -> chunked(2)
                    .map { it.toInt(16).toByte() }
                    .toByteArray()
            }

        fun ByteArray.toHexString(): String =
            when(isNotEmpty()) {
                true -> joinToString("") { "%02x".format(it) }
                else -> ""
            }

        fun String.toEncodeUrl(): String =
            when (isEmpty()) {
                true -> runCatching {
                    URLEncoder.encode(this, CHARSET_UTF8.name())
                }.getOrDefault(this)
                else -> this
            }

        fun String.toDecodeUrl(): String =
            when(isNotEmpty()) {
                true -> runCatching {
                    URLDecoder.decode(this, CHARSET_UTF8.name())
                }.getOrDefault(this)
                else -> this
            }

        fun String.formatUsageMessageFormat(vararg args: Any): String =
            MessageFormat.format(this, args)
    }
}