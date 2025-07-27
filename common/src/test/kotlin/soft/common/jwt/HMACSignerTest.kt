package soft.common.jwt

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import soft.common.encoder.toBase64String
import java.security.SecureRandom


class HMACSignerTest {
    private val secretKey_soft = "dZE898BFiFmQMduRMYy0Y7DNoCkXV3nwx2H066CpMLg="
    private val secretKey_ssun = "avJ9eJthHmyApTGZV2gXxxCbueW8ZPXc49HEci+PBB4="

    @Test
    fun `HMAC signature works`() {
        val soft_byteArray = ByteArray(32)
        val ssun_byteArray = ByteArray(32)

        val secureRandom = SecureRandom()
        secureRandom.nextBytes(soft_byteArray)
        secureRandom.nextBytes(ssun_byteArray)

        println(soft_byteArray.toBase64String())
        println(ssun_byteArray.toBase64String())

    }

    @Test
    fun `HMACSigner_객체_Cache 확인`() {
        val exprected_signer = HMACSigner.of("SOFT_MHAC", secretKey_soft)
        val accture_signer = HMACSigner.of("SOFT_MHAC", secretKey_soft)

        Assertions.assertEquals(exprected_signer.hashCode(), accture_signer.hashCode())
    }

    @Test
    fun `HMACSigner_객체_Cache_생성 및 검증`() {
        val signer = HMACSigner.of("SOFT_MHAC", secretKey_soft)
        val jwt = signer.serialize {
            it.claim("SOFT", "SOFT_VALUE")
        }

        Assertions.assertNotNull(jwt)
        Assertions.assertTrue(jwt.isNotBlank())
        Assertions.assertTrue(signer.verify(jwt))
    }

    @Test
    fun `HMACSigner_CLAIMS_체크`() {
        val signer = HMACSigner.of("SOFT_MHAC", secretKey_soft)
        val jwt = signer.serialize {
            it.claim("SOFT", "SOFT_VALUE")
        }

        val claimsSet = signer.getClaimsSetWithVerify(jwt)
        val claims = claimsSet.claims


        Assertions.assertEquals(claims["SOFT"], "SOFT_VALUE")
    }

    @Test
    fun `HMACSigner_서로 다른 Signer 로 검증 실패`() {
        val signer: ISigner = HMACSigner.of("SOFT", secretKey_soft)
        val jwt = signer.serialize {
            it.claim("BODY_1", "BODY_1_VALUE")
        }

        val differentSigner = HMACSigner.of("SSUN", secretKey_ssun)
        val verify = differentSigner.verify(jwt)

        Assertions.assertFalse(verify)
    }

    @Test
    fun `HMACSigner_서로 다른 Signer 로 Claimset 획득 실패`() {
        val signer: ISigner = HMACSigner.of("SOFT", secretKey_soft)
        val jwt = signer.serialize {
            it.claim("BODY_1", "BODY_1_VALUE")
        }

        val differentSigner = HMACSigner.of("SSUN", secretKey_ssun)

        val exception = Assertions.assertThrows(IllegalAccessError::class.java) {
            differentSigner.getClaimsSetWithVerify(jwt)
        }

        Assertions.assertEquals("failed to jwt verify", exception.message)
    }
}