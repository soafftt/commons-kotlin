package soft.common.crypt

import soft.common.crypt.enums.CryptStringMode
import soft.common.crypt.enums.HmacAlgorithm
import soft.common.string.Base64Mode
import soft.common.string.toBase64Array
import soft.common.string.toBase64String
import java.nio.charset.Charset
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

@OptIn(ExperimentalStdlibApi::class)
fun String.matchHmac(
    targetHashString: String,
    key: ByteArray,
    hmacAlgorithm: HmacAlgorithm,
    stringMode: CryptStringMode,
): Boolean {
    val sourceHashBuffer = this.computeHmacHash(key, hmacAlgorithm)
    val targetHashBuffer = when(stringMode) {
        CryptStringMode.BASE64 -> targetHashString.toBase64Array()
        CryptStringMode.BASE64_MIME -> targetHashString.toBase64Array(mode = Base64Mode.MIME)
        CryptStringMode.HEX -> targetHashString.hexToByteArray()
    }

    if (sourceHashBuffer.size != targetHashBuffer.size) {
        return false
    }

    return targetHashBuffer.contentEquals(sourceHashBuffer)
}

@OptIn(ExperimentalStdlibApi::class)
fun String.computeHmacHash(
    key: ByteArray,
    hmacAlgorithm: HmacAlgorithm,
    charset: Charset = Charsets.UTF_8,
    toStringMode: CryptStringMode = CryptStringMode.BASE64
): String {
    val hashBuffer = computeHmacHash(key, hmacAlgorithm, charset)
    return when (toStringMode) {
        CryptStringMode.BASE64 -> hashBuffer.toBase64String()
        CryptStringMode.BASE64_MIME -> hashBuffer.toBase64String(mode = Base64Mode.MIME)
        CryptStringMode.HEX -> hashBuffer.toHexString()
    }
}

fun String.computeHmacHash(
    key: ByteArray,
    hmacAlgorithm: HmacAlgorithm,
    charset: Charset = Charsets.UTF_8,
): ByteArray =
    toByteArray(charset).computeHmacHash(key, hmacAlgorithm)

@OptIn(ExperimentalStdlibApi::class)
fun ByteArray.computeHash(
    key: ByteArray,
    hmacAlgorithm: HmacAlgorithm,
    toStringMode: CryptStringMode = CryptStringMode.BASE64
): String {
    val hashBuffer = computeHmacHash(key, hmacAlgorithm)
    return when (toStringMode) {
        CryptStringMode.BASE64 -> hashBuffer.toBase64String()
        CryptStringMode.BASE64_MIME -> hashBuffer.toBase64String(mode = Base64Mode.MIME)
        CryptStringMode.HEX -> hashBuffer.toHexString()
    }
}

fun ByteArray.computeHmacHash(
    key: ByteArray,
    hmacAlgorithm: HmacAlgorithm,
): ByteArray =
    key.makeMac(hmacAlgorithm).doFinal(this)


private fun ByteArray.makeMac(hmacAlgorithm: HmacAlgorithm): Mac =
    Mac.getInstance(hmacAlgorithm.algorithmName).also {
        val keySpec = SecretKeySpec(this, hmacAlgorithm.name)
        it.init(keySpec)
    }