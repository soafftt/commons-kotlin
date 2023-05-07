package soft.commons.enums

enum class CommonsErrorCode(val code: Int) {
    SUCCESS(0),
    // usual errorCode (1 ~ 100)
    INVALID_ARGUMENT(1),

    // exceptional errorCode (101 ~ .......)
    // JSON (101 ~ 110)
    JSON_SERIALIZE_ERROR(101),
    JSON_DESERIALIZE_ERROR(102),




    UNKNOWN(Integer.MIN_VALUE);


    companion object {
        private val commonsErrorCodeMap: Map<Int, CommonsErrorCode> =
            CommonsErrorCode.values().associateBy { it.code }

        fun lookup(code: Int, defaultCommonsErrorCode: CommonsErrorCode = CommonsErrorCode.UNKNOWN) =
            commonsErrorCodeMap.getOrDefault(code, defaultCommonsErrorCode)
    }
}
