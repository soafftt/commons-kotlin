package soft.common.crypto.keygenerator

import java.security.SecureRandom
import javax.crypto.spec.SecretKeySpec

class SecureRandomKeyGenerator(val keyLength: Int = DEFAULT_KEY_LENGTH) {

    private val secureRandom: SecureRandom = SecureRandom()

    companion object {
        private const val DEFAULT_KEY_LENGTH: Int = 8
//        private val logger = LoggerFactory.getLogger(SecureRandomKeyGenerator::class.java)
    }

    fun generateKey(secret: ByteArray? = null): ByteArray = try {
        if (secret != null) {
            SecretKeySpec(secret, "AES").encoded.copyOfRange(0, this.keyLength)
        } else {
            ByteArray(this.keyLength).let {
                this.secureRandom.nextBytes(it)
                it
            }
        }
    } catch (ex: Exception) {
//        logger.error(ex.message, ex)

        ByteArray(this.keyLength)
    }
}