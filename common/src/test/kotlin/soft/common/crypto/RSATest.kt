package soft.common.crypto

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import soft.common.crypto.enums.CryptStringMode
import soft.common.crypto.utils.RsaKeyPairUtil

class RSATest {
    private val defaultKeyPair = RsaKeyPairUtil.getDefaultKeyPair()

    @Test
    fun testRSA() {
        val planText = "SOFT"
        val cryptBuffer = planText.cryptToRSA(
            key = defaultKeyPair.public,
            cryptStringMode = CryptStringMode.BASE64
        )

        val decryptString  = cryptBuffer.decryptToRSA(
            key = defaultKeyPair.private,
            cryptStringMode = CryptStringMode.BASE64,
            charset = Charsets.UTF_8
        )

        Assertions.assertEquals(planText, decryptString)
    }

    @Test
    fun testRSA_mismatch() {
        val planText = "SOFT"
        val mismatchPlanText = "SOFT_2"
        val cryptBuffer = planText.cryptToRSA(
            key = defaultKeyPair.public,
            cryptStringMode = CryptStringMode.BASE64
        )

        val mismatchCryptString = mismatchPlanText.cryptToRSA(
            key = defaultKeyPair.public,
            cryptStringMode = CryptStringMode.BASE64
        )

        val decryptString  = mismatchCryptString.decryptToRSA(
            key = defaultKeyPair.private,
            cryptStringMode = CryptStringMode.BASE64,
            charset = Charsets.UTF_8
        )

        Assertions.assertNotEquals(planText, decryptString)
    }

    @Test
    fun testRSA_mismatch_cryptStringMode() {
        val planText = "SOF_TTTTTTTTTTTTTTTT_SOF_TTTTTTTTTTTTTTTT_SOF_TTTTTTTTTTTTTTTT_SOF_TTTTTTTTTTTTTTTT"
        val cryptBuffer = planText.cryptToRSA(
            key = defaultKeyPair.public,
            cryptStringMode = CryptStringMode.HEX
        )




        Assertions.assertThrows(Exception::class.java) {
            cryptBuffer.decryptToRSA(
                key = defaultKeyPair.private,
                cryptStringMode = CryptStringMode.BASE64,
                charset = Charsets.UTF_8
            )
        }
    }
}