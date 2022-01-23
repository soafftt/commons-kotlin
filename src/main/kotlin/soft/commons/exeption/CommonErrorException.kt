package soft.commons.exeption

import soft.commons.consts.STRING_EMPTY
import soft.commons.consts.STRING_PIPELINE

class CommonErrorException : RuntimeException {
    var code: Int = 0
        private set

    var description: String = ""
        private set

    private constructor() {
    }

    constructor(code: Int, message: String? = "", throwable: Throwable? = null) : super(message, throwable) {
        this.code = code
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