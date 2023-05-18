package soft.commons.extenstions.crypt

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import soft.commons.crypto.AesCipher
import soft.commons.extenstions.StringExtension.Companion.toBase64Array
import soft.commons.extenstions.StringExtension.Companion.toBase64String
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class AesExtension {
    companion object {
        private val logger: Logger = LoggerFactory.getLogger(AesExtension::class.java)

        fun String.encryptToAesCbc(key: ByteArray): String =
            try {
                AesCipher.cbcInit()

                val secretKey = SecretKeySpec(key, "AES")
                val ivParameterSpec = IvParameterSpec(secretKey.encoded)

                AesCipher.cbcCipher!!.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);
                AesCipher.cbcCipher!!.doFinal(toByteArray()).toBase64String()

            } catch (ex: Exception) {
                logger.error("", ex)
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