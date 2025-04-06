package soft.common.crypt

import soft.common.crypt.enums.AESTransformation
import soft.common.crypt.enums.CryptStringMode
import soft.common.string.Base64Mode
import soft.common.string.toBase64Array
import soft.common.string.toBase64String
import java.nio.charset.Charset
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

private const val AES_KEY_SPEC_ALGORITHM = "AES"

/***
 * @this planText
 * @param key encrypt Key
 * @param transformation support AES encrypt mode
 * @param charset
 * @param mode AES encrypt 이후 String 결과 타입 (HEX, Base64, Default)
 */
@OptIn(ExperimentalStdlibApi::class)
fun String.encryptAES(
    key: ByteArray,
    transformation: AESTransformation,
    charset: Charset = Charsets.UTF_8,
    mode: CryptStringMode = CryptStringMode.BASE64
): String {
    val encryptedBuffer = this.encryptAES(key, transformation, charset)
    return when (mode) {
        CryptStringMode.HEX -> encryptedBuffer.toHexString()
        CryptStringMode.BASE64 -> encryptedBuffer.toBase64String()
        CryptStringMode.BASE64_MIME -> encryptedBuffer.toBase64String(mode = Base64Mode.MIME)
    }
}

fun String.encryptAES(
    key: ByteArray,
    transformation: AESTransformation,
    charset: Charset = Charsets.UTF_8,
): ByteArray =
    this.toByteArray(charset).encryptAES(key, transformation)


fun ByteArray.encryptAES(key: ByteArray, transformation: AESTransformation): ByteArray =
    key.makeAESCipher(transformation, Cipher.ENCRYPT_MODE).doFinal(this)


@OptIn(ExperimentalStdlibApi::class)
fun String.decryptAES(
    key: ByteArray,
    transformation: AESTransformation,
    fromStringMode: CryptStringMode = CryptStringMode.BASE64
): ByteArray {
    val sourceBuffer = when (fromStringMode) {
        CryptStringMode.HEX -> this.hexToByteArray()
        CryptStringMode.BASE64 -> this.toBase64Array()
        CryptStringMode.BASE64_MIME -> this.toBase64Array(mode = Base64Mode.MIME)
    }

    return sourceBuffer.decryptAES(key, transformation)
}

fun ByteArray.decryptAES(
    key: ByteArray,
    transformation: AESTransformation,
): ByteArray =
    key.makeAESCipher(transformation, Cipher.DECRYPT_MODE)
        .doFinal(this)


private fun ByteArray.makeAESCipher(
    transformation: AESTransformation,
    cipherMode: Int = Cipher.ENCRYPT_MODE
): Cipher =
    Cipher.getInstance(transformation.transformationName).also { cipher ->
        val keySpec = SecretKeySpec(this, AES_KEY_SPEC_ALGORITHM)
        val ivParameterSpec = IvParameterSpec(keySpec.encoded)

        cipher.init(cipherMode, keySpec, ivParameterSpec)
    }