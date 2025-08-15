package soft.common.crypto.enums

enum class RSATransformation(val transformationName: String) {
    RSA("RSA"),
    RSA_ECB_PKCS1PADDING("RSA/ECB/PKCS1Padding"),
    RSA_ECB_OAEP_WITH_SHA_1_AND_MGF1PADDING("RSA/ECB/OAEPWithSHA-1AndMGF1Padding"),
    RSA_ECB_OAEP_WITH_SHA_256_AND_MGF1_PADDING("RSA/ECB/OAEPWithSHA-256AndMGF1Padding"),
    ;
}