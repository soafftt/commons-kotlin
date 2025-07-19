package soft.common.jwt

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnore

open class JwtBaseClaims @JsonCreator constructor (
    @JsonIgnore val issuer: String? = null,
    @JsonIgnore val expiresIn: Long? = 1 * 1000 * 60 * 60 * 24 // 1 day
)