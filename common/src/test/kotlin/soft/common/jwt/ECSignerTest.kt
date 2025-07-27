package soft.common.jwt

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ECSignerTest {

    private val ecKey_SOFT_P256 = "{\"kty\":\"EC\",\"d\":\"tWE35D185ewrRoXTSkdpCnj4Il4ZBY9VJlgI9JRSCRo\",\"crv\":\"P-256\",\"kid\":\"SOFT\",\"x\":\"us4Cfwsi2sz8AJ4gQBGQl_ltnyujYvnmqjiLAaYTsLU\",\"y\":\"FT-b81JzE1ZYFnO5rXRTJG7EUK0DYcuEl4oEu_a3CLI\"}\n"
    private val ecKey_SSUN_P256 = "{\"kty\":\"EC\",\"d\":\"ti60VL8QXIR6fnI9x9Lrhh6nHJNKt1SOQyO0RITnv8s\",\"crv\":\"P-256\",\"kid\":\"SSUN\",\"x\":\"HflkJW1gRLntMQAjS0wqNcZbAJrg9p_9Ey8UcFWclys\",\"y\":\"O9WmItNs4Mtyj1n4gP0lgZqz-zeGcgdTPHraebdRmos\"}\n"

    @Test
    fun `ECSigner_객체_Cache 확인`() {
        val exprected_ecSigner: ISigner = ECSigner.of("SOFT", ecKey_SOFT_P256)
        val acture_ecSigner: ISigner = ECSigner.of("SOFT", ecKey_SOFT_P256)

        Assertions.assertEquals(exprected_ecSigner.hashCode(), acture_ecSigner.hashCode())
    }

    @Test
    fun `ECSigner_생성 및 검증`() {
        val ecSigner: ISigner = ECSigner.of("SOFT", ecKey_SOFT_P256)

        val jwt = ecSigner.serialize {
            it.claim("BODY_1", "BODY_1_VALUE")
        }

        Assertions.assertNotNull(jwt)
        Assertions.assertTrue(jwt.isNotBlank())

        val verify = ecSigner.verify(jwt)
        Assertions.assertTrue(true)
    }

    @Test
    fun `ECSigner_CLAIMS_체크`() {
        val ecSigner: ISigner = ECSigner.of("SOFT", ecKey_SOFT_P256)
        val jwt = ecSigner.serialize {
            it.claim("BODY_1", "BODY_1_VALUE")
        }

        val claimsSet = ecSigner.getClaimsSetWithVerify(jwt)
        val claims = claimsSet.claims

        Assertions.assertEquals(claims["BODY_1"], "BODY_1_VALUE")
    }

    @Test
    fun `ECSigner_서로 다른 Signer 로 검증 실패`() {
        val ecSigner: ISigner = ECSigner.of("SOFT", ecKey_SOFT_P256)
        val jwt = ecSigner.serialize {
            it.claim("BODY_1", "BODY_1_VALUE")
        }

        val differentSigner = ECSigner.of("SSUN", ecKey_SSUN_P256)
        val verify = differentSigner.verify(jwt)

        Assertions.assertFalse(verify)
    }

    @Test
    fun `ECSigner_서로 다른 Signer 로 Claimset 획득 실패`() {
        val ecSigner: ISigner = ECSigner.of("SOFT", ecKey_SOFT_P256)
        val differentSigner = ECSigner.of("SSUN", ecKey_SSUN_P256)
        val jwt = ecSigner.serialize {
            it.claim("BODY_1", "BODY_1_VALUE")
        }

        val exception = Assertions.assertThrows(IllegalAccessError::class.java) {
            differentSigner.getClaimsSetWithVerify(jwt)
        }

        Assertions.assertEquals("failed to jwt verify", exception.message)
    }
}