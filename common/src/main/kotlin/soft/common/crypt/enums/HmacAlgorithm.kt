package soft.common.crypt.enums

enum class HmacAlgorithm(val algorithmName: String) {
    MD5("HmacMD5"),
    SHA1("HmacSHA1"),
    SHA224("HmacSHA224"),
    SHA384("HmacSHA384"),
    SHA512("HmacSHA512"),
}