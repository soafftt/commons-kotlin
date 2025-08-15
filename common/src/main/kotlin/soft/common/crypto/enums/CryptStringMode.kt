package soft.common.crypto.enums

import soft.common.encoder.Base64Mode
import soft.common.encoder.toBase64Array
import soft.common.encoder.toBase64String
import kotlin.text.hexToByteArray
import kotlin.text.toHexString

enum class CryptStringMode {
    BASE64,
    BASE64_MIME,
    HEX,
    ;
}

@OptIn(ExperimentalStdlibApi::class)
fun CryptStringMode.toStringFromByteArray(source: ByteArray): String =
    when (this) {
        CryptStringMode.HEX -> source.toHexString()
        CryptStringMode.BASE64 -> source.toBase64String()
        CryptStringMode.BASE64_MIME -> source.toBase64String(mode = Base64Mode.MIME)
    }

@OptIn(ExperimentalStdlibApi::class)
fun CryptStringMode.toByteArrayFromString(source: String) : ByteArray =
    when (this) {
        CryptStringMode.HEX -> source.hexToByteArray()
        CryptStringMode.BASE64 -> source.toBase64Array()
        CryptStringMode.BASE64_MIME -> source.toBase64Array(mode = Base64Mode.MIME)
    }