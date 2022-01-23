package soft.commons.enums

enum class CommonErrorCode(val code: Int) {
    UNKNOWN(Integer.MIN_VALUE),
    JWT_ENCODE(Integer.MIN_VALUE + 1),
    JWT_DECODE(Integer.MIN_VALUE + 2),

    SUCCESS(0),
    INVALID_PARAMETER(1),
    PBKDF2_EMPTY_SECRET(2),

    // 101 ~ 200 MemberAPI
    MEMBER_API_NOT_EXISTS_MEMBER(101),
    MEMBER_API_INACTIVE_MEMBER(102),
    MEMBER_API_NOT_EXISTS_RESULT(103),
    MEMBER_API_INVALID_AUTH_REQUEST(104),

    MEMBER_API_UNKNOWN(200),
    ;

    companion object {
        private val codesMap: Map<Int, CommonErrorCode> = values().associateBy { it.code }

        fun lookup(code: Int): CommonErrorCode = codesMap.getOrDefault(code, UNKNOWN)
    }
}