package soft.common.jwt

import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.JWSSigner
import com.nimbusds.jose.JWSVerifier
import com.nimbusds.jose.crypto.ECDSASigner
import com.nimbusds.jose.crypto.ECDSAVerifier
import com.nimbusds.jose.jwk.Curve
import com.nimbusds.jose.jwk.ECKey
import java.security.cert.X509Certificate


class ECSigner private constructor(
    ecKey: ECKey,
    curve: Curve,
    algorithm: JWSAlgorithm,
) : AbstractSigner() {

    private val ecPublicKey: ECKey = ecKey.toPublicJWK()

    override val signer: JWSSigner = ECDSASigner(ecKey)
    override val verifier: JWSVerifier = ECDSAVerifier(ecPublicKey)
    override val algorithm: JWSAlgorithm = algorithm

    companion object {
        fun of(
            signerName: String,
            secretKey: String,
            curve: Curve = Curve.P_256,
            algorithm: JWSAlgorithm = JWSAlgorithm.ES256,
        ) : ISigner {
            return getSignerOrPutIfAbsent(signerName) {
                ECSigner(ECKey.parse(secretKey), curve, algorithm)
            }
        }

        fun of(
            signerName: String,
            x509Certificate: X509Certificate,
            curve: Curve = Curve.P_256,
            algorithm: JWSAlgorithm = JWSAlgorithm.ES256,
        )  : ISigner {
            return getSignerOrPutIfAbsent(signerName) {
                ECSigner(ECKey.parse(x509Certificate), curve, algorithm)
            }
        }
    }
}