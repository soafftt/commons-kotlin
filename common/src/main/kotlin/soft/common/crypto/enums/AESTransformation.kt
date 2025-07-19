package soft.common.crypto.enums

enum class AESTransformation(val transformationName: String) {
    CBC("AES/CBC/PKCS5Padding"),
    CTR("AES/CTR/NoPadding"),
    RFC2898("AES/CBC/PKCS5Padding")
}