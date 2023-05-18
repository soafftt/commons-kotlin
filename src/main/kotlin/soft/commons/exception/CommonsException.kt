package soft.commons.exception

import soft.commons.enums.ErrorCode
import java.lang.RuntimeException

class CommonsException(
    val code: ErrorCode,
    private val throwable: Throwable? = null
): RuntimeException(throwable) {
    constructor(code: Int, throwable: Throwable? = null): this(ErrorCode.lookup(code), throwable) {}
}
