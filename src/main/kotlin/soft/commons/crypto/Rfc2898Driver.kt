package soft.commons.crypto

import java.nio.charset.StandardCharsets
import java.security.InvalidKeyException
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import kotlin.experimental.xor

/*
* reference C# Rfc2898Driver
* */
class Rfc2898Driver(
    password: ByteArray,
    private val salt: ByteArray,
    private val iterations: Int
) {
    companion object {
        private fun getBytesFromInt(i: Int): ByteArray =
            byteArrayOf((i ushr 24).toByte(), (i ushr 16).toByte(), (i ushr 8).toByte(), i.toByte())
    }

    private var hmacSha1: Mac
    private var buffer = ByteArray(20)
    private var bufferStartIndex = 0
    private var bufferEndIndex = 0
    private var block = 1

    init {
        if (salt.size < 8) {
            throw InvalidKeyException("Salt must be 8 bytes or more.")
        }

        hmacSha1 = Mac.getInstance("HmacSHA1")
        hmacSha1.init(SecretKeySpec(password, "HmacSHA1"))
    }

    constructor(password: String, salt: ByteArray, iterations: Int) :
            this(password.toByteArray(StandardCharsets.UTF_8), salt, iterations)

    constructor(password: String, salt: ByteArray) :
            this(password.toByteArray(StandardCharsets.UTF_8), salt, 0x3e8)

    fun getBytes(count: Int): ByteArray {
        val result = ByteArray(count)
        var resultOffset = 0
        val bufferCount = bufferEndIndex - bufferStartIndex

        if (bufferCount > 0) { // if there is some data in buffer
            if (count < bufferCount) { // if there is enough data in buffer
                // if there is enough data in buffer
                System.arraycopy(buffer, bufferStartIndex, result, 0, count)
                bufferStartIndex += count
                return result
            }

            System.arraycopy(buffer, bufferStartIndex, result, 0, bufferCount)

            bufferEndIndex = 0
            bufferStartIndex = 0
            resultOffset += bufferCount
        }

        while (resultOffset < count) {
            val needCount = count - resultOffset
            buffer = func()
            if (needCount > 20) { // we one (or more) additional passes
                // we one (or more) additional passes
                // we one (or more) additional passes
                System.arraycopy(buffer, 0, result, resultOffset, 20)
                resultOffset += 20
            } else {
                System.arraycopy(buffer, 0, result, resultOffset, needCount)
                bufferStartIndex = needCount
                bufferEndIndex = 20
                return result
            }
        }
        return result
    }

    private fun func(): ByteArray {
        hmacSha1.update(this.salt, 0, this.salt.size)
        var tempHash = hmacSha1.doFinal(getBytesFromInt(block))
        hmacSha1.reset()

        val finalHash = tempHash
        for (i in 2..this.iterations) {
            tempHash = hmacSha1.doFinal(tempHash)
            for (j in 0..19) {
                finalHash[j] = (finalHash[j] xor tempHash[j])
            }
        }

        block = when (block) {
            2147483647 -> -2147483648
            else -> block + 1
        }

        return finalHash
    }
}