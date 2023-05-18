package soft.commons.crypto

import soft.commons.HMAC_SHA_256_ALGORITHM
import java.util.concurrent.locks.ReentrantLock
import javax.crypto.Mac
import kotlin.concurrent.withLock

class Hmac {
    companion object {
        private val mac256Lock = ReentrantLock()

        var mac256: Mac? = null
            private set

        fun initHmac256(): Unit {
            if(mac256 == null) {
                mac256Lock.withLock {
                    if(mac256 == null) {
                        mac256 = Mac.getInstance(HMAC_SHA_256_ALGORITHM);
                    }
                }
            }
        }
    }
}