package soft.common.crypto

import soft.common.crypto.enums.CryptStringMode
import soft.common.crypto.enums.HmacAlgorithm
import soft.common.crypto.enums.toByteArrayFromString
import soft.common.crypto.enums.toStringFromByteArray
import soft.common.encoder.Base64Mode
import soft.common.encoder.toBase64Array
import soft.common.encoder.toBase64String
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

    return if (sourceHashBuffer.size != targetHashBuffer.size) {
        return false
    } else {
        targetHashBuffer.contentEquals(sourceHashBuffer)
    }
}

@OptIn(ExperimentalStdlibApi::class)
fun String.computeHmacHash(
    key: ByteArray,
    hmacAlgorithm: HmacAlgorithm,
    charset: Charset = Charsets.UTF_8,
    toStringMode: CryptStringMode = CryptStringMode.BASE64
): String {
    val hashBuffer = computeHmacHash(key, hmacAlgorithm, charset)
    return toStringMode.toStringFromByteArray(hashBuffer)
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
    return toStringMode.toStringFromByteArray(hashBuffer)
}

fun ByteArray.computeHmacHash(
    key: ByteArray,
    hmacAlgorithm: HmacAlgorithm,
): ByteArray =
    key.makeMac(hmacAlgorithm).doFinal(this)


private fun ByteArray.makeMac(hmacAlgorithm: HmacAlgorithm): Mac =
    makeMacInstance(hmacAlgorithm).also {
        val keySpec = SecretKeySpec(this, hmacAlgorithm.name)
        it.init(keySpec)
    }

fun makeMacInstance(hmacAlgorithm: HmacAlgorithm): Mac =
    Mac.getInstance(hmacAlgorithm.algorithmName)