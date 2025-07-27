import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.nimbusds.jwt.JWTClaimsSet
import org.junit.jupiter.api.Test
import soft.common.json.writeJson
import soft.common.jwt.claims.JwtBaseClaims
import soft.common.jwt.ECSigner
import soft.common.jwt.toObject

class JwsClaimsTest {

    @Test
    fun test_11() {
        val claims = TestClaims("1", "2") as SuperClaims
        val f = test(claims)
        val jwtClaims = JWTClaimsSet.Builder()
            .build()
    }

    @Test
    fun ecSigner() {
        val signer = ECSigner.of("ABCDEF", "1")
        val jwt = signer.serialize({ it.build() }) {
            it.claim("name", "ABCDEF")
            it.claim("b", "ABCDEF")
            it.issuer("BCCDDFF")
            it.build()
        }
        println(jwt)

        println(signer.verify(jwt))

        val claimsSet = signer.getClaimsSetWithVerify(jwt)
        val d = claimsSet.toObject(TestClaims::class.java)

        println(d)
    }


    private fun <T : SuperClaims> test(cf: T) : String {
        val d = cf.writeJson(false)
        return d
    }
}

open class SuperClaims


@JsonIgnoreProperties(ignoreUnknown = true)
class TestClaims @JsonCreator constructor (
    @JsonProperty("cd", defaultValue = "null")
    val c: String?,
    @JsonProperty("de", defaultValue = "null")
    val d: String?
) : JwtBaseClaims()