package soft.common.crypto

import soft.common.encoder.toBase64Array
import soft.common.encoder.toBase64String
import java.security.PrivateKey
import java.security.PublicKey
import javax.crypto.Cipher

fun String.encryptToRsa(key: PublicKey): String = try {
    val cipher = makeCipher()
    cipher.init(Cipher.ENCRYPT_MODE, key)

    cipher.doFinal(this.toByteArray()).toBase64String()
} catch(ex: Exception) {
    this
}

fun String.decryptToRsa(key: PrivateKey): String = try {
    val cipher = makeCipher()
    cipher.init(Cipher.DECRYPT_MODE, key)

    String(cipher.doFinal(this.toBase64Array()))
} catch(ex: Exception) {
    this
}

private fun makeCipher() : Cipher {
    return Cipher.getInstance("RSA")
}

fun ByteArray.toPublicKey(): PublicKey = RsaKeyPairUtil.makePublicKey(this)
fun ByteArray.toPrivateKey(): PrivateKey = RsaKeyPairUtil.makePrivateKey(this)