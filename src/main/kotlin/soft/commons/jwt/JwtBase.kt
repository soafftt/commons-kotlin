package soft.commons.jwt

abstract class JwtBase {
    abstract fun getClaims(): Map<String, Any?>
}