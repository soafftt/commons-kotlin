package soft.common.jwt

import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.JWSSigner

/**
 * InstanceOf 로 객체를 생성할 수 있음,
 * 재 사용이 필요한 객체로 Spring 과 함께 사용한다면 Bean @Configuration 으로 bean 을 등록해서 사용하길 권장.
 */
class Jwt (
    private val signer: JWSSigner,
    private val expireMillis: Long = 0,
) {

    companion object {
    }

    fun <T: JwtBaseClaims> serialize(): String {
        return ""
    }
}