package soft.common.jwt.claims

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.nimbusds.jwt.JWTClaimNames
import java.util.Date

@JsonIgnoreProperties(ignoreUnknown = true)
open class JwtBaseClaims {
    @JsonProperty(JWTClaimNames.ISSUER)
    var issuer: String? = null
        private set

    @JsonProperty(JWTClaimNames.SUBJECT)
    var subject: String? = null
        private set

    @JsonProperty(JWTClaimNames.AUDIENCE)
    var audience: List<String>? = null
        private set

    @JsonProperty(JWTClaimNames.EXPIRATION_TIME)
    var expiredAt: Date? = null
        private set

    @JsonProperty(JWTClaimNames.NOT_BEFORE)
    var notBefore: Date? = null
        private set

    @JsonProperty(JWTClaimNames.ISSUED_AT)
    var issuedAt: Date? = null
        private set

    @JsonProperty(JWTClaimNames.JWT_ID)
    var jwtId: String? = null
        private set
}