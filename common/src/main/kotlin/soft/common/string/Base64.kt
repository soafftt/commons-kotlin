package soft.common.string

import java.nio.charset.Charset
import java.util.*

enum class Base64Mode {
    DEFAULT,
    MIME,
    ;
}

private val base64Encoder = Base64.getEncoder()
private val base64Decoder = Base64.getDecoder()

private val base64MimeEncoder = Base64.getMimeEncoder()
private val base64MimeDecoder = Base64.getMimeDecoder()


fun String.toBase64String(mode: Base64Mode = Base64Mode.DEFAULT, fromCharset: Charset = Charsets.UTF_8): String =
    toByteArray(fromCharset).let {
        if (mode == Base64Mode.DEFAULT) {
            base64Encoder.encodeToString(it)
        } else {
            base64MimeEncoder.encodeToString(it)
        }
    }

fun String.toBase64Array(mode: Base64Mode = Base64Mode.DEFAULT): ByteArray =
    if (mode == Base64Mode.DEFAULT) {
        base64Decoder.decode(this)
    } else {
        base64MimeDecoder.decode(this)
    }


fun ByteArray.toBase64Array(mode: Base64Mode = Base64Mode.DEFAULT): ByteArray =
    if (mode == Base64Mode.DEFAULT) {
        base64Encoder.encode(this)
    } else {
        base64MimeEncoder.encode(this)
    }


fun ByteArray.toBase64String(mode: Base64Mode = Base64Mode.DEFAULT): String =
    if (mode == Base64Mode.DEFAULT) {
        base64Encoder.encodeToString(this)
    } else {
        base64MimeEncoder.encodeToString(this)
    }
