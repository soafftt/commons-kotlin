package soft.common.jwt.signer

import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.JWSSigner
import com.nimbusds.jose.JWSVerifier
import com.nimbusds.jose.crypto.ECDSASigner
import com.nimbusds.jose.crypto.ECDSAVerifier
import com.nimbusds.jose.jwk.Curve
import com.nimbusds.jose.jwk.ECKey
import com.nimbusds.jose.jwk.gen.ECKeyGenerator


class ECSigner private constructor(
    keyId: String,
    curve: Curve,
    algorithm: JWSAlgorithm,
) : AbstractSigner() {

    private val ecJWK: ECKey = ECKeyGenerator(curve).keyID(keyId).generate()
    private val ecPublicKey: ECKey = ecJWK.toPublicJWK()

    override val signer: JWSSigner = ECDSASigner(ecJWK)
    override val verifier: JWSVerifier = ECDSAVerifier(ecPublicKey)
    override val algorithm: JWSAlgorithm = algorithm

    companion object {
        fun of(
            signerName: String,
            keyId: String,
            curve: Curve = Curve.P_256,
            algorithm: JWSAlgorithm = JWSAlgorithm.ES256,
        ): ISigner {
            return getSignerOrPutIfAbsent(signerName) {
                ECSigner(keyId, curve, algorithm)
            }
        }
    }
}