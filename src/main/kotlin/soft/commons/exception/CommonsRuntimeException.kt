package soft.commons.exception

import soft.commons.enums.CommonsErrorCode
import java.lang.RuntimeException

class CommonsRuntimeException(
    val code: CommonsErrorCode,
    private val throwable: Throwable? = null
): RuntimeException(throwable) {
    constructor(code: Int, throwable: Throwable? = null): this(CommonsErrorCode.lookup(code), throwable) {}
}
