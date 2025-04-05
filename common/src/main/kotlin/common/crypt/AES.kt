package common.crypt

import common.string.Base64Mode
import common.string.toBase64Array
import common.string.toBase64String
import java.nio.charset.Charset
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

private const val AES_KEY_SPEC_ALGORITHM = "AES"

object AES {
    enum class Transformation(val transformation: String) {
        CBC("AES/CBC/PKCS5Padding"),
        CTR("AES/CTR/NoPadding"),
        RFC2898("AES/CBC/PKCS5Padding")
    }

    enum class StringMode {
        DEFAULT,
        BASE64,
        BASE64_MIME,
        HEX,

    }

    val instanceOf: (Transformation) -> Cipher = { transformation ->
        Cipher.getInstance(transformation.transformation)
    }
}

@OptIn(ExperimentalStdlibApi::class)
fun String.encryptAES(
    key: ByteArray,
    transformation: AES.Transformation,
    charset: Charset = Charsets.UTF_8,
    mode: AES.StringMode = AES.StringMode.DEFAULT
): String {
    val encryptedBuffer = this.encryptAES(key, transformation, charset)
    return when (mode) {
        AES.StringMode.HEX -> encryptedBuffer.toHexString()
        AES.StringMode.BASE64 -> encryptedBuffer.toBase64String()
        AES.StringMode.BASE64_MIME -> encryptedBuffer.toBase64String(mode = Base64Mode.MIME)
        else -> String(encryptedBuffer, charset)
    }
}

fun String.encryptAES(
    key: ByteArray,
    transformation: AES.Transformation,
    charset: Charset = Charsets.UTF_8,
): ByteArray =
    this.toByteArray(charset).encryptAES(key, transformation)


fun ByteArray.encryptAES(key: ByteArray, transformation: AES.Transformation): ByteArray {
    val cipher = AES.instanceOf(transformation)

    val keySpec = SecretKeySpec(key, AES_KEY_SPEC_ALGORITHM)
    val ivParameterSpec = IvParameterSpec(keySpec.encoded)

    cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParameterSpec)
    return cipher.doFinal(this)
}

@OptIn(ExperimentalStdlibApi::class)
fun String.decryptAES(
    key: ByteArray,
    transformation: AES.Transformation,
    charset: Charset = Charsets.UTF_8,
    fromStringMode: AES.StringMode = AES.StringMode.DEFAULT,
    toStringMode: AES.StringMode = AES.StringMode.DEFAULT
): String {
    val decryptBuffer = this.decryptAES(key, transformation, charset, fromStringMode)

    return when (toStringMode) {
        AES.StringMode.HEX -> decryptBuffer.toHexString()
        AES.StringMode.BASE64 -> decryptBuffer.toBase64String()
        AES.StringMode.BASE64_MIME -> decryptBuffer.toBase64String(mode = Base64Mode.MIME)
        else -> String(decryptBuffer, charset)
    }
}

@OptIn(ExperimentalStdlibApi::class)
fun String.decryptAES(
    key: ByteArray,
    transformation: AES.Transformation,
    charset: Charset = Charsets.UTF_8,
    fromStringMode: AES.StringMode = AES.StringMode.DEFAULT
): ByteArray {
    val sourceBuffer = when (fromStringMode) {
        AES.StringMode.HEX -> this.hexToByteArray()
        AES.StringMode.BASE64 -> this.toBase64Array()
        AES.StringMode.BASE64_MIME -> this.toBase64Array(mode = Base64Mode.MIME)
        else -> this.toByteArray(charset)
    }

    return sourceBuffer.decryptAES(key, transformation)
}

fun ByteArray.decryptAES(
    key: ByteArray,
    transformation: AES.Transformation,
): ByteArray {
    val cipher = AES.instanceOf(transformation)

    val keySpec = SecretKeySpec(key, AES_KEY_SPEC_ALGORITHM)
    val ivParameterSpec = IvParameterSpec(keySpec.encoded)

    cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParameterSpec)
    return cipher.doFinal(this)
}

