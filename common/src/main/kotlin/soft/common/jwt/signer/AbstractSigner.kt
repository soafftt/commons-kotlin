package soft.common.jwt.signer

import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.JWSHeader
import com.nimbusds.jose.JWSSigner
import com.nimbusds.jose.JWSVerifier
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.SignedJWT
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

abstract class AbstractSigner : ISigner {
    protected abstract val signer: JWSSigner
    protected abstract val verifier: JWSVerifier
    protected abstract val algorithm: JWSAlgorithm

    companion object {
        private val signerMap: MutableMap<String, ISigner> = mutableMapOf()
        private val lock = ReentrantLock()

        internal fun getSignerOrPutIfAbsent(
            signerName: String,
            ifAbsentSigner: () -> ISigner,
        ) : ISigner {
            return if (signerMap.containsKey(signerName)) {
                signerMap[signerName]!!
            } else {
                lock.withLock {
                    signerMap[signerName] ?: run {
                        ifAbsentSigner.invoke().apply { signerMap[signerName] = this }
                    }
                }
            }
        }
    }

    override fun serialize(
        jwsHeadersBuilder: ((JWSHeader.Builder) -> Unit)?,
        jwtClaimsSetBuilder: ((JWTClaimsSet.Builder) -> Unit)?
    ): String {
        val jwsHeader = JWSHeader.Builder(algorithm).let { headerBuilder ->
            jwsHeadersBuilder?.invoke(headerBuilder)
            headerBuilder.build()
        }

        val jwtClaimsSet = JWTClaimsSet.Builder().let { claimsSetBuilder ->
            jwtClaimsSetBuilder?.invoke(claimsSetBuilder)
            claimsSetBuilder.build()
        }

        val signedJWT = SignedJWT(jwsHeader, jwtClaimsSet).apply { sign(signer) }
        return signedJWT.serialize()
    }

    override fun getClaimsSetWithVerify(jwt: String): JWTClaimsSet =
        SignedJWT.parse(jwt).apply { verify(verifier) }.jwtClaimsSet


    override fun verify(jwt: String): Boolean =
        SignedJWT.parse(jwt).verify(verifier)
}