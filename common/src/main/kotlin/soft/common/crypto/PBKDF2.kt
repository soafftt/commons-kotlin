package soft.common.crypto

import soft.common.concatenate
import soft.common.crypto.keygenerator.SecureRandomKeyGenerator
import soft.common.encoder.UTF8Encoder
import soft.common.encoder.toBase64Array
import soft.common.encoder.toBase64String
import soft.common.subArray
import soft.common.toHexArray
import java.security.MessageDigest
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

class PBKDF2(
    secret: CharSequence,
    saltLength: Int = DEFAULT_HASH_SALT_LENGTH,

    private val iterations: Int = DEFAULT_HASH_ITERATIONS,
    private val hashWidth: Int = DEFAULT_HASH_WIDTH,
    private val algorithm: KeyFactoryAlgorithm = KeyFactoryAlgorithm.PBKDF2WithHmacSHA256,
    private val encodeHashAsBase64: Boolean = false
) {
    companion object {
        const val DEFAULT_HASH_SALT_LENGTH = 8
        const val DEFAULT_HASH_WIDTH = 256
        const val DEFAULT_HASH_ITERATIONS = 185000
    }

    private val secretBytes: ByteArray = UTF8Encoder.encode(secret)
    private val saltGenerator: SecureRandomKeyGenerator = SecureRandomKeyGenerator(saltLength)

    fun encode(raw: CharSequence): String {
        val salt = this.saltGenerator.generateKey();
        val encodedBytes = computeHash(raw, salt);

        return toEncodeString(encodedBytes);
    }

    fun match(raw: CharSequence, hashString: String): Boolean =
        toDecodeByteArray(hashString).let {
            val salt = it.subArray(0, this.saltGenerator.keyLength)
            MessageDigest.isEqual(it, computeHash(raw, salt))
        }

    @OptIn(ExperimentalStdlibApi::class)
    private fun toEncodeString(encodedArray: ByteArray): String =
        when (this.encodeHashAsBase64) {
            true -> encodedArray.toBase64String()
            else -> encodedArray.toHexString()
        }

    private fun toDecodeByteArray(encodedString: String): ByteArray =
        when (this.encodeHashAsBase64) {
            true -> encodedString.toBase64Array()
            else -> encodedString.toHexArray()
        }

    private fun computeHash(raw: CharSequence, salt: ByteArray): ByteArray =
        runCatching {
            SecretKeyFactory.getInstance(this.algorithm.name)
                .let {
                    val spec = PBEKeySpec(
                        raw.toString().toCharArray(),
                        arrayOf(salt, this.secretBytes).concatenate(),
                        this.iterations,
                        this.hashWidth
                    )

                    arrayOf(salt, it.generateSecret(spec).encoded).concatenate()
                }
        }.onFailure {
            throw IllegalStateException("Could not create hash", it)
        }.getOrThrow()

    enum class KeyFactoryAlgorithm {
        PBKDF2WithHmacSHA1, PBKDF2WithHmacSHA256, PBKDF2WithHmacSHA512
    }
}