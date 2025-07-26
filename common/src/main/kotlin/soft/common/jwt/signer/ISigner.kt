package soft.common.jwt.signer

import com.nimbusds.jose.JWSHeader
import com.nimbusds.jwt.JWTClaimsSet
import soft.common.json.readJsonToObject

interface ISigner {
    fun serialize(
        jwsHeadersBuilder: ((builder: JWSHeader.Builder) -> Unit)? = null,
        jwtClaimsSetBuilder: ((builder: JWTClaimsSet.Builder) -> Unit)? = null
    ) : String

    fun getClaimsSetWithVerify(jwt: String): JWTClaimsSet

    fun verify(jwt: String): Boolean

}

fun <T> JWTClaimsSet.toObject(cls: Class<T>): T {
    return this.toString().readJsonToObject(cls, false)!!
}