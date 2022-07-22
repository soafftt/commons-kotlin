package soft.commons.exeption

import soft.commons.consts.STRING_EMPTY
import soft.commons.consts.STRING_PIPELINE

class ErrorException(
    code: Int,
    message: String? = "",
    throwable: Throwable? = null
) : RuntimeException(message, throwable) {
    var code: Int = code
        private set

    private var description: String = ""

    init {
        this.description = String.format(
                "code=%s|description=%s",
                code.toString(),
                if (!message.isNullOrEmpty()) {
                    message
                } else {
                    STRING_EMPTY
                }
        )
        this.description += if (throwable != null) {
            STRING_PIPELINE + throwable.message
        } else {
            STRING_EMPTY
        }
    }
}