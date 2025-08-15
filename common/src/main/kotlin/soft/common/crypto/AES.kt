package soft.common.crypto

import soft.common.crypto.enums.AESTransformation
import soft.common.crypto.enums.CryptStringMode
import soft.common.crypto.enums.toByteArrayFromString
import soft.common.crypto.enums.toStringFromByteArray
import soft.common.encoder.Base64Mode
import soft.common.encoder.toBase64Array
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
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
    charset: Charset = StandardCharsets.UTF_8,
    mode: CryptStringMode = CryptStringMode.BASE64
): String {
    val encryptedBuffer = this.encryptAES(key, transformation, charset)
    return mode.toStringFromByteArray(encryptedBuffer)
}

fun String.encryptAES(
    key: ByteArray,
    transformation: AESTransformation,
    charset: Charset = StandardCharsets.UTF_8,
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
    val sourceBuffer = fromStringMode.toByteArrayFromString(this)
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