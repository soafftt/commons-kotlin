package soft.commons.extension

import soft.commons.extension.CommonExtension.Companion.padRight
import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.text.MessageFormat
import java.util.*
import kotlin.math.ceil

class CommonExtension {
    companion object {
        private val CHARSET_UTF8: Charset = StandardCharsets.UTF_8;
        private val base64Encoder = Base64.getMimeEncoder()
        private val base64Decoder = Base64.getMimeDecoder()

        //<editor-fold desc="base64 Encoding/Decoding">
        fun String.toBase64String(): String =
            base64Encoder.encodeToString(toByteArray())

        fun String.toBase64Array(): ByteArray =
            base64Decoder.decode(this)

        fun ByteArray.toBase64Array(): ByteArray =
            base64Encoder.encode(this)

        fun ByteArray.toBase64String(): String =
            base64Encoder.encodeToString(this)
        //</editor-fold>

        //<editor-fold desc="hexString">
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
        //</editor-fold>

        //<editor-fold desc="url Encode/Decode">
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
        //</editor-fold>

        fun String.padRight(replaceChar: Char = '*'): String =
            runCatching {
                val digit = ceil(length / 2.0).toInt()
                val subString = this.substring(0, length - digit)

                subString.padStart(length, replaceChar)
            }.getOrDefault(this)

        fun String.padLeft(replaceChar: Char = '*'): String =
            runCatching {
                val digit = ceil(length / 2.0).toInt()
                val subString = this.substring(digit, length)

                subString.padStart(length, replaceChar)
            }.getOrDefault(this)

        fun String.formatUsageMessageFormat(vararg args: Any): String =
            MessageFormat.format(this, args)
    }
}