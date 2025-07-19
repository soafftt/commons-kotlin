import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.nimbusds.jwt.JWTClaimsSet
import org.junit.jupiter.api.Test
import soft.common.json.writeJson

class JwsClaimsTest {

    @Test
    fun test_11() {
        val claims = TestClaims("1", "2") as SuperClaims
        val f = test(claims)
        val jwtClaims = JWTClaimsSet.Builder()

            .build()
        println(f)
    }


    private fun <T : SuperClaims> test(cf: T) : String {
        val d = cf.writeJson(false)
        return d
    }
}

open class SuperClaims


class TestClaims @JsonCreator constructor (
    @JsonProperty("cd")
    val c: String,
    @JsonProperty("de")
    val d: String
) : SuperClaims()