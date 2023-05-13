package soft.commons.extenstions.crypt

import soft.commons.extenstions.StringExtension.Companion.toBase64Array
import soft.commons.extenstions.StringExtension.Companion.toBase64String
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
    }
}