package soft.commons.util

import soft.commons.extension.JsonExtension.Companion.decodeJson
import org.junit.jupiter.api.Test

class RsaKeypairUtilTest {
    @Test
    fun test_makePublicKey_success() {
        val key = RsaKeyPairUtil.getDefaultKeyPair()
    }
}