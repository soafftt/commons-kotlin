package soft.commons.extension

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import soft.commons.crypto.Rfc2898Driver
import soft.commons.extension.CommonExtension.Companion.toBase64Array
import soft.commons.extension.CommonExtension.Companion.toBase64String
import java.nio.charset.StandardCharsets
import java.util.concurrent.locks.ReentrantLock
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import kotlin.concurrent.withLock

class AesExtension {
    private class AesCipher {
        companion object {
            private val aesLock = ReentrantLock()

            var cbcCipher: Cipher? = null
                private set
            var ctrCipher: Cipher? = null
                private set
            var rfc2898Cipher: Cipher? = null
                private set

            fun cbcInit(): Unit {
                if (cbcCipher == null) {
                    aesLock.withLock {
                        if (cbcCipher == null) {
                            cbcCipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
                        }
                    }
                }
            }

            fun ctrInit(): Unit {
                if (ctrCipher == null) {
                    aesLock.withLock {
                        if (ctrCipher == null) {
                            ctrCipher = Cipher.getInstance("AES/CTR/PKCS5Padding")
                        }
                    }
                }
            }

            fun rfc2898Init(): Unit {
                if (rfc2898Cipher == null) {
                    aesLock.withLock {
                        if (rfc2898Cipher == null) {
                            rfc2898Cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
                        }
                    }
                }
            }
        }
    }

    companion object {
        fun String.encryptToAesCbc(key: ByteArray): String =
                try {
                    AesCipher.cbcInit()

                    val secretKey = SecretKeySpec(key, "AES")
                    val ivParameterSpec = IvParameterSpec(secretKey.encoded)

                    AesCipher.cbcCipher!!.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);
                    AesCipher.cbcCipher!!.doFinal(toByteArray()).toBase64String()

                } catch (ex: Exception) {
                    this
                }

        fun String.decryptToAesCbc(key: ByteArray): String =
                try {
                    AesCipher.cbcInit()

                    val secretKey = SecretKeySpec(key, "AES")
                    val ivParameterSpec = IvParameterSpec(secretKey.encoded)

                    AesCipher.cbcCipher!!.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);

                    String(AesCipher.cbcCipher!!.doFinal(this.toBase64Array()))
                } catch (ex: Exception) {
                    this
                }

        fun String.encryptToAesCtr(key: ByteArray): String =
                try {
                    AesCipher.ctrInit()

                    val secretKey = SecretKeySpec(key, "AES")
                    val ivParameterSpec = IvParameterSpec(secretKey.encoded)

                    AesCipher.ctrCipher!!.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);

                    AesCipher.ctrCipher!!.doFinal(this.toByteArray()).toBase64String()
                } catch (ex: Exception) {
                    this
                }

        fun String.decryptToAesCtr(key: ByteArray): String =
                try {
                    AesCipher.ctrInit()

                    val secretKey = SecretKeySpec(key, "AES")
                    val ivParameterSpec = IvParameterSpec(secretKey.encoded)

                    AesCipher.ctrCipher!!.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);

                    String(AesCipher.ctrCipher!!.doFinal(this.toBase64Array()))
                } catch (ex: Exception) {
                    this
                }

        fun String.encryptToAesCbcRfc289(pwd: String, salt: String): String =
                try {
                    AesCipher.rfc2898Init()

                    val pwdBytes = pwd.toByteArray(StandardCharsets.UTF_8)
                    val saltBytes = salt.toByteArray(StandardCharsets.UTF_8)

                    val rfc2898DeriveBytes = Rfc2898Driver(pwdBytes, saltBytes, 100)
                    val keys = rfc2898DeriveBytes.getBytes(32)
                    val iv = rfc2898DeriveBytes.getBytes(16)

                    AesCipher.rfc2898Cipher!!.init(Cipher.ENCRYPT_MODE, SecretKeySpec(keys, "AES"), IvParameterSpec(iv));

                    AesCipher.rfc2898Cipher!!.doFinal(toByteArray(StandardCharsets.UTF_8)).toBase64String()
                } catch (ex: Exception) {
                    this
                }

        fun String.decryptToAesCbcRfc289(pwd: String, salt: String): String =
                try {
                    AesCipher.rfc2898Init()

                    val pwdBytes = pwd.toByteArray(StandardCharsets.UTF_8)
                    val saltBytes = salt.toByteArray(StandardCharsets.UTF_8)

                    val rfc2898DeriveBytes = Rfc2898Driver(pwdBytes, saltBytes, 100)
                    val keys = rfc2898DeriveBytes.getBytes(32)
                    val iv = rfc2898DeriveBytes.getBytes(16)

                    AesCipher.rfc2898Cipher!!.init(Cipher.DECRYPT_MODE, SecretKeySpec(keys, "AES"), IvParameterSpec(iv));

                    String(
                            AesCipher.rfc2898Cipher!!.doFinal(toBase64Array()),
                            StandardCharsets.UTF_8
                    )
                } catch (ex: Exception) {
                    this
                }
    }
}