package soft.commons.crypto

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class PBKDF2Test {
    private val secret: CharSequence = "fakeSecret"

    @Test
    fun test_encode_success() {
        val rawString = "testString"

        val actualEncodingString = PBKDF2(secret).encode(rawString)

        Assertions.assertNotNull(actualEncodingString)
        Assertions.assertFalse(actualEncodingString.isEmpty())
    }

    @Test
    fun test_match_success() {
        val rawString = "testString"
        val hashString = PBKDF2(secret).encode(rawString)

        val actualMatch = PBKDF2(secret).match(rawString, hashString)

        Assertions.assertTrue(actualMatch)
    }

    @Test
    fun test_match_mismatchRawString_returnFalse() {
        val misMatchRawString = "misMatchString"
        val rawString = "testString"

        val hashString = PBKDF2(secret).encode(rawString)

        val actualMatch = PBKDF2(secret).match(misMatchRawString, hashString)

        Assertions.assertFalse(actualMatch)
    }

    @Test
    fun test_match_mismatchSecret_returnFalse() {
        val rawString = "testString"
        val misMatchSecret = "mismatchSecret"

        val hashString = PBKDF2(secret).encode(rawString)

        val actualMatch = PBKDF2(misMatchSecret).match(rawString, hashString)

        Assertions.assertFalse(actualMatch)
    }

    @Test
    fun test_match_mismatchSaltLength_returnFalse() {
        val rawString = "testString"
        val mismatchSaltLength = 16

        val hashString = PBKDF2(secret).encode(rawString)

        val actualMatch = PBKDF2(secret, saltLength = mismatchSaltLength).match(rawString, hashString)

        Assertions.assertFalse(actualMatch)

    }

    @Test
    fun test_match_mismatchIterations_returnFalse() {
        val rawString = "testString"
        val mismatchIterationCounts = 3000

        val hashString = PBKDF2(secret).encode(rawString)

        val actualMatch = PBKDF2(secret, iterations = mismatchIterationCounts).match(rawString, hashString)

        Assertions.assertFalse(actualMatch)
    }

    @Test
    fun test_match_mismatchHashAlgorithm_returnFalse() {
        val rawString = "testString"
        val mismatchHashAlgorithm = PBKDF2.KeyFactoryAlgorithm.PBKDF2WithHmacSHA1

        val hashString = PBKDF2(secret).encode(rawString)

        val actualMatch = PBKDF2(secret, algorithm = mismatchHashAlgorithm).match(rawString, hashString)

        Assertions.assertFalse(actualMatch)
    }

    @Test
    fun test_match_mismatchEncodeStringType_returnFalse() {
        val rawString = "testString"
        val encodeHashAsBase64 = true

        val hashString = PBKDF2(secret).encode(rawString)

        val actualMatch = PBKDF2(secret, encodeHashAsBase64 = encodeHashAsBase64).match(rawString, hashString)

        Assertions.assertFalse(actualMatch)
    }
}