package soft.commons.crypto

import java.security.SecureRandom
import javax.crypto.spec.SecretKeySpec

class SecureRandomKeyGenerator(val keyLength: Int = DEFAULT_KEY_LENGTH) {
    companion object {
        private const val DEFAULT_KEY_LENGTH: Int = 8
    }

    private val secureRandom: SecureRandom = SecureRandom()

    fun generateKey(secret: ByteArray? = null): ByteArray = try {
        if (secret != null) {
            SecretKeySpec(secret, "AES").encoded.copyOfRange(0, this.keyLength)
        } else {
            val bytes: ByteArray = ByteArray(this.keyLength)
            this.secureRandom.nextBytes(bytes)

            bytes
        }
    } catch (ex: Exception) {
        ByteArray(this.keyLength)
    }
}