package soft.common

import kotlinx.coroutines.CancellationException
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.text.MessageFormat
import kotlin.random.Random

fun String.toHexString(): String {
    return toHexArray().toHexString()
}

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

fun String.toEncodeUrl(charSet: Charset = StandardCharsets.UTF_8): String =
    when (isEmpty()) {
        true -> runCatching {
            URLEncoder.encode(this, charSet)
        }.getOrElse {
            if (it is CancellationException) throw it
            this
        }
        else -> this
    }

fun String.toDecodeUrl(charSet: Charset = StandardCharsets.UTF_8): String =
    when(isNotEmpty()) {
        true -> runCatching {
            URLDecoder.decode(this, charSet)
        }.getOrElse {
            if (it is CancellationException) throw it
            this
        }
        else -> this
    }

fun String.formatUsageMessageFormat(vararg args: Any): String =
    MessageFormat.format(this, args)

fun randomString(
    charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9'),
    length: Int,
    seed: Long? = null
): String  {
    val random = if (seed != null) Random(seed) else Random.Default
    val randomString = (1..length)
        .map { i ->
            charPool[random.nextInt(charPool.size)]
        }
        .joinToString("")

    return randomString
}