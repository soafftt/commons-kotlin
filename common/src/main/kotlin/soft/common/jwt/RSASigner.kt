package soft.common.jwt

import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.JWSSigner
import com.nimbusds.jose.JWSVerifier
import com.nimbusds.jose.crypto.RSASSASigner
import com.nimbusds.jose.crypto.RSASSAVerifier
import com.nimbusds.jose.jwk.RSAKey
import java.security.cert.X509Certificate

class RSASigner private constructor(
    rsaKey: RSAKey,
    algorithm: JWSAlgorithm,
) : AbstractSigner() {

    override val signer: JWSSigner = RSASSASigner(rsaKey)
    override val verifier: JWSVerifier = RSASSAVerifier(rsaKey)
    override val algorithm: JWSAlgorithm = algorithm

    companion object {
        fun of(
            signerName: String,
            secretKey: String,
            algorithm: JWSAlgorithm = JWSAlgorithm.RS256,
        ) : ISigner {
            return getSignerOrPutIfAbsent(signerName) {
                RSASigner(RSAKey.parse(secretKey), algorithm)
            }
        }

        fun of(
            signerName: String,
            x509Certificate: X509Certificate,
            algorithm: JWSAlgorithm = JWSAlgorithm.RS256,
        )  : ISigner {
            return getSignerOrPutIfAbsent(signerName) {
                RSASigner(RSAKey.parse(x509Certificate), algorithm)
            }
        }
    }
}
