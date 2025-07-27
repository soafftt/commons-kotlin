package soft.common.jwt

import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.JWSSigner
import com.nimbusds.jose.JWSVerifier
import com.nimbusds.jose.crypto.MACSigner
import com.nimbusds.jose.crypto.MACVerifier

class HMACSigner private constructor(
    secretByteArray: ByteArray,
    algorithm: JWSAlgorithm,
) : AbstractSigner() {

    override val signer: JWSSigner = MACSigner(secretByteArray)
    override val verifier: JWSVerifier = MACVerifier(secretByteArray)
    override val algorithm: JWSAlgorithm = algorithm

    companion object {
        fun of(
            signerName: String,
            secretKey: String,
            algorithm: JWSAlgorithm = JWSAlgorithm.HS256,
        ): ISigner {
            return getSignerOrPutIfAbsent(signerName) {
                HMACSigner(secretKey.toByteArray(), algorithm)
            }
        }
    }
}