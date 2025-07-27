package soft.common.jwt

import com.nimbusds.jose.jwk.gen.RSAKeyGenerator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class RSASignerTest {

    private val rsakey_soft = "{\"p\":\"6amFxtrk89D_IpdY8fMtSN77fc4VB_nerh5lzf8iiM7fH8BKgdrt7RUEs9em-tl_-qvYSFICOpYXgVrYZcOpgr1lVahN_QFL6Sf_4kNb2I0nSCgRGODJCBQ0Omemn6weaOgwdu0nxjU3Pc21_vKEg6_EnnMAIetGo_8jGhibYdc\",\"kty\":\"RSA\",\"q\":\"ybfB4XxOvJ1M0CLpmv34agZpkrZpIRUi_YeROiah5cibFE9o4YBQsfASBYykxGRfjwyTHTZ1SucXofqVWxqnK-8Q4mSyR9BsFFkdrWYpAtdpUqJ9NXRRadSTSs1q49z0LlrzrN83abrC-fOfoFtIDUNGpunghKq_YYzRp67j4oE\",\"d\":\"WYysD8TrQnu6x98anID8cHudXLptuewB9X6rCYS9UdgRy7PdmmNwuWizgXhJqBzyFD4qVcmWMDIQUQmc7sk6SLkrx4_OKD16nzFB7Hu8cdEF_yqbIl6-nx-3wsbgEkAU-arwmrr8TecTwmlOkCJhD9Cbyau9wPWdlk2XAt3ZQwGOq4KAvDdKlM5ybBkWqgEuDv1UE1h0RugUjzwZcoHV6y3jFT7l83Xguvh-Fw7vsIwSFzePgkQqjw_YiNUGmLcMMT69tvdqtUtSz4WExqppOc1VXUytadl5_EC7vPRSSlEiAuCica0D2m_wgDuTpi1y0MrZDjPt82Bz7f9fgGzwgQ\",\"e\":\"AQAB\",\"qi\":\"iF9TcFoiNlrV36uCyZgQeIlbk__YEEyenWT6ZZ9hLdSLTIrQqYBYIkKh32RrLN9N1oChHuXqZ5qtwrAiYclrG9p044IzwcibAXoy2z5h7UCMgrSrANiiPwBnfrVmKVywCKRuujyuh1cSDieKGKkVgJ6BZo_y-8wNF80cqliqStw\",\"dp\":\"mEh7f1Y50Q1sukaZGVa7NLrefOdKnaAjmKcHAnI9N5xbnn3cETV1ywXfwrXRhbsqxVwp4ThRa41WVF-4mNokJ6j32hOOgE3QA2ymP_hWa5PS_hveyK_3VOSWBVp4JagvSX_J1jhJq8wS7Jyc-ENmSczL_9hDRdfoeglV_zP6UJE\",\"dq\":\"TlYRcirfPPpteSsFw447qZkh6Lr0KsfVldXO_WFBYEY-i22nUmONcvzDUupilm5QefItM5wRguwpzlrNI9s-7p3h1bm7To5PWruc_dj9tj_HhDLcIyUv9MR8CwGpMCJ_pXzmwszQ7Tt9Oqp05EZuvc_RoUKUUZEpz41rYg5WK4E\",\"n\":\"uB3TLmBTtoGHsl55tdw-k0Da5wRie2HEddsFN8FQmB96xp43z6kH0TSRXe-Rr56-PuUIHX13y9z4yOSeAowU4X85FoUyKuRX4h9va177IB3f7sktVxgIj2y66DqFwN4bHpgndoBzuKlYGklr5CvYGjH9xxANyYj5KSZGgo4Im701J0eWVyE9Yk9tcgTcUtWF_JidFPJzJlcVIRPE4v5osmeRp481fB15RpwF9kvPK-nUxEROa_kyUbnuR91Ldy_k3ersuCfvwgm3kSgRhVkFhsZBNS_kcdaVZ700QgLg-ugqF_djZ3655-NmiNr316f96k6uFCBaDwmePIJodlEbVw\"}\n"
    private val rsakey_ssun = "{\"p\":\"uYnfGSOUMVU2DkQhF9YjcgdGtKiHrMseCU_Z58hmh46SGY8i2vYvjQREp4PgbdTkpdTw4C1CfJhNcE3HS51tbU_N8YUnRuL1g6J9kojh81YTg97g25tQ7EHTJeP7H_zbe5V-7pmWcWuwNcTusmjIgtlkY246Or_wattON4l-h88\",\"kty\":\"RSA\",\"q\":\"tfRZCfaOsYGQ6Yr-aRQvcGKHMeitZmeHU0Rubm8FoKRzZQ2KzIhevJtkpfy2S76p_psOceVpqqV4SeAcRJiseG-4ym_zKE7EKxXPgDwJ0dyCa7kmI4n84jlBCepxKn3g7zBz7I3ddJcQ0jsnWkH0lBC9CHwos3faMwfYC1UZR3E\",\"d\":\"CVupjPzr1O5KEsa7VJZG-V5-jTsaslXIx-aeBasFNbf_oMiVEU89XlPj8lnAyTP3se6L_K4joQGOqB4ANKSKp7_xOs9goy2LB4jUhn3wjD1NM4UKGfEKki5FBD5uJzgyADytd1CFOLmHNigBirCYla50eJ-ySGl2QGhAW-fWBlOczCvlvFIlczkpAYbnMitEFPuhob-4320g4qfc2rwVjDwXV3rTO-5k-4_GouKLOYyvzqITWTkC1rRALUie1kx18SbnCwwHvEBMGneDJaWhRhxa0YCwmPB-IfVZTLwqIkR4ltqKDMuiMN6UJttb5RSkPEbOG5e1l8U0iIQHlMbx\",\"e\":\"AQAB\",\"qi\":\"ng7NO5aduSC2zLvJf7EA3GIuXllT_IR4_6fkVI15iHGT6L0PLDxeLcrR_RqlbAqGHSyt9P4p5zWcmGC7UhXfoyoLz1jR_s3UGHVWLR2ZIT-rwFQ-7SoNYhtqQmg2i7sBHm_ZNiv-pQhg0aiaOunzDBeq9qeyJgSM6DzACP-qMo0\",\"dp\":\"HixfzdTNauBytAs8XoYCqa6pPwQRXHaD4fvMcjdTBUSYWfO_NWDIBuTkzWFTBEmVDar9qVdy8pRKM7NSb-egPiC2W0rOQmbyin_IcCY2qL4c2ltKsiwmEuX5krXqxx72CrkbNW5g2v0qYPQv5W_I_A6iltD8Q0WnsZkMtVd07l0\",\"dq\":\"o6IduQINDcGRyxX--gz3JMDXCKFR3_hXESFHlvUtQGzkggqNuqchW8j72PLOFoxfYOOdN_yy7I5nATkU8vPCl4UR0KyhJqy_amfw9kgIJL2EZNBbZjagP-pxfTNjlm0iLrmKZ0Cl2kZJEGe2WktaYxYyikDucYflqWLyfi6Hz2E\",\"n\":\"g9-SrZE4LOMsrrkku69QtugRrEZO1N7ZtRq_gH9sqh8euNGkp5OY0fpA7kI6SolxpVWcNt6Mk6lgPSx-rXjqXVqfauVTz3WgR41Jppgq1eZkJ8GhaWuyrDrRdZ71J7BcWqSSIMUX_sHCCaUe-V2JvXOkNMelb-mdUFujdUuOSMGbhJHIi5kAfgpIi00x2uZPo1RuDliYb3QOxOkwyA-yDT14pLlmZfwi1Q-Wv-t3A0I9bXVyseKKv6FopicqkugTajcJ3OfMa7Vx9GPXT2LBu9mCjvkFQPXUMZUZm-9PrXK9TgwnTQzL-8AiImhnHPhRYxrygZsRtlSwQdv9xrtbXw\"}"


    @Test
    fun `HMAC signature works`() {
        val generator = RSAKeyGenerator(2048)
        println(generator.generate())
        println(generator.generate())
    }

    @Test
    fun `RSASigner_객체_Cache 확인`() {
        val expected_signer = RSASigner.of("SOFT", rsakey_soft)
        val acture_signer = RSASigner.of("SOFT", rsakey_soft)

        Assertions.assertEquals(expected_signer.hashCode(), acture_signer.hashCode())
    }

    @Test
    fun `RSASigner_객체_Cache_생성 및 검증`() {
        val signer = RSASigner.of("SOFT", rsakey_soft)
        val jwt = signer.serialize {
            it.claim("SOFT", "SOFT_VALUE")
        }

        Assertions.assertNotNull(jwt)
        Assertions.assertTrue(jwt.isNotBlank())
        Assertions.assertTrue(signer.verify(jwt))
    }

    @Test
    fun `RSASigner_CLAIMS_체크`() {
        val signer = RSASigner.of("SOFT", rsakey_soft)
        val jwt = signer.serialize {
            it.claim("SOFT", "SOFT_VALUE")
        }

        val claimsSet = signer.getClaimsSetWithVerify(jwt)
        val claims = claimsSet.claims


        Assertions.assertEquals(claims["SOFT"], "SOFT_VALUE")
    }

    @Test
    fun `RSASigner_서로 다른 Signer 로 검증 실패`() {
        val signer: ISigner = RSASigner.of("SOFT", rsakey_soft)
        val jwt = signer.serialize {
            it.claim("BODY_1", "BODY_1_VALUE")
        }

        val differentSigner = RSASigner.of("SSUN", rsakey_ssun)
        val verify = differentSigner.verify(jwt)

        Assertions.assertFalse(verify)
    }

    @Test
    fun `RSASigner_서로 다른 Signer 로 Claimset 획득 실패`() {
        val signer: ISigner = RSASigner.of("SOFT", rsakey_soft)
        val jwt = signer.serialize {
            it.claim("BODY_1", "BODY_1_VALUE")
        }

        val differentSigner = RSASigner.of("SSUN", rsakey_ssun)

        val exception = Assertions.assertThrows(IllegalAccessError::class.java) {
            differentSigner.getClaimsSetWithVerify(jwt)
        }

        Assertions.assertEquals("failed to jwt verify", exception.message)
    }
}