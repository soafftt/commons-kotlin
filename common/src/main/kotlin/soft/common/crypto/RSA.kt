package soft.common.crypto

import soft.common.crypto.enums.CryptStringMode
import soft.common.crypto.enums.RSATransformation
import soft.common.crypto.enums.toByteArrayFromString
import soft.common.crypto.enums.toStringFromByteArray
import soft.common.crypto.utils.RsaKeyPairUtil
import java.nio.charset.Charset
import java.security.PrivateKey
import java.security.PublicKey
import javax.crypto.Cipher

fun String.cryptToRSA(
    publicKeyArray: ByteArray,
    transformation: RSATransformation = RSATransformation.RSA,
    cryptStringMode: CryptStringMode = CryptStringMode.BASE64
): String {
    val publicKey = publicKeyArray.toPublicKey()
    return this.cryptToRSA(publicKey, transformation, cryptStringMode)
}

fun String.cryptToRSA(
    key: PublicKey,
    transformation: RSATransformation = RSATransformation.RSA,
    cryptStringMode: CryptStringMode = CryptStringMode.BASE64
): String = try {
    val cryptBuffer = this.cryptToRSA(key, transformation)
    cryptStringMode.toStringFromByteArray(cryptBuffer)
} catch(ex: Exception) {
    throw ex
}

fun String.cryptToRSA(
    keyArray: ByteArray,
    transformation: RSATransformation = RSATransformation.RSA
) : ByteArray {
    val publicKey = keyArray.toPublicKey()
    return this.cryptToRSA(publicKey, transformation)
}

fun String.cryptToRSA(
    key: PublicKey,
    transformation: RSATransformation = RSATransformation.RSA
): ByteArray =
    try {
        val cipher = makeCipher(transformation) {
            it.init(Cipher.ENCRYPT_MODE, key)
        }

        cipher.doFinal(this.toByteArray())
    } catch(ex: Exception) {
        throw ex
    }


fun String.decryptToRSA(
    key: PrivateKey,
    transformation: RSATransformation = RSATransformation.RSA,
    cryptStringMode: CryptStringMode = CryptStringMode.BASE64,
    charset: Charset = Charsets.UTF_8
): String = try {
    val decryptBuffer = this.decryptToRSA(key, transformation, cryptStringMode)

    String(decryptBuffer, charset)
} catch(ex: Exception) {
    throw ex
}

fun String.decryptToRSA(
    key: PrivateKey,
    transformation: RSATransformation = RSATransformation.RSA,
    cryptStringMode: CryptStringMode = CryptStringMode.BASE64
): ByteArray = try {
    val cipher = makeCipher(transformation) {
        it.init(Cipher.DECRYPT_MODE, key)
    }

    cipher.doFinal(cryptStringMode.toByteArrayFromString(this))
} catch(ex: Exception) {
    throw ex
}

fun String.matchRSA(
    key: PrivateKey,
    transformation: RSATransformation = RSATransformation.RSA,
    cryptStringMode: CryptStringMode = CryptStringMode.BASE64
) : Boolean {
    val cipher = makeCipher(transformation) {
        it.init(Cipher.DECRYPT_MODE, key)
    }

    val sourceBuffer = cryptStringMode.toByteArrayFromString(this)
    val decryptBuffer = cipher.doFinal(cryptStringMode.toByteArrayFromString(this))

    return sourceBuffer.contentEquals(decryptBuffer)
}

private fun makeCipher(
    transformation: RSATransformation = RSATransformation.RSA,
    cipherInit: (cipher: Cipher) -> Unit
) : Cipher {
    return Cipher.getInstance(transformation.transformationName).apply {
        cipherInit(this)
    }
}

fun ByteArray.toPublicKey(): PublicKey = RsaKeyPairUtil.makePublicKey(this)
fun ByteArray.toPrivateKey(): PrivateKey = RsaKeyPairUtil.makePrivateKey(this)