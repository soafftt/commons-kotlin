package soft.commons.extension

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
        private val base64Encoder = Base64.getEncoder()
        private val base64Decoder = Base64.getDecoder()

        //<editor-fold desc="base64 Encoding/Decoding">
        fun String.toBase64String(): String = base64Encoder.encodeToString(toByteArray())

        fun String.toBase64Array(): ByteArray = base64Decoder.decode(this)

        fun ByteArray.toBase64Array(): ByteArray = base64Encoder.encode(this)

        fun ByteArray.toBase64String(): String = base64Encoder.encodeToString(this)
        //</editor-fold>

        //<editor-fold desc="hexString">
        fun String.toHexArray(): ByteArray =
            when (this) {
                "" -> ByteArray(0)
                else -> chunked(2)
                    .map { it.toInt(16).toByte() }
                    .toByteArray()
            }

        fun ByteArray.toHexString(): String =
            if (this.isEmpty()) {
                ""
            } else {
                joinToString("") { "%02x".format(it) }
            }
        //</editor-fold>

        //<editor-fold desc="url Encode/Decode">
        fun String.toEncodeUrl(): String =
            when {
                isNotEmpty() -> try {
                    URLEncoder.encode(this, CHARSET_UTF8.name())
                } catch (e: UnsupportedEncodingException) {
                    this
                }
                else -> this
            }

        fun String.toDecodeUrl(): String =
            when {
                isNotEmpty() -> try {
                    URLDecoder.decode(this, CHARSET_UTF8.name())
                } catch (e: UnsupportedEncodingException) {
                    this
                }
                else -> this
            }
        //</editor-fold>

        fun String.toHiddenString(): String =
            try {
                val digit = ceil(length / 2.0).toInt()
                val subString = this.substring(0, length - digit)

                String.format("%-" + length + "s", subString).replace(' ', '*')
            } catch (e: Exception) {
                this
            }

        fun String.formatUsageMessageFormat(vararg args: Any): String =
            MessageFormat.format(this, args)
    }
}