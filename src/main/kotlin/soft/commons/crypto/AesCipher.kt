package soft.commons.crypto

import java.util.concurrent.locks.ReentrantLock
import javax.crypto.Cipher
import kotlin.concurrent.withLock

class AesCipher {
    companion object {
        private val aesLock = ReentrantLock()

        var cbcCipher: Cipher? = null
            private set
        var ctrCipher: Cipher? = null
            private set
        var rfc2898Cipher: Cipher? = null
            private set

        fun cbcInit(): Unit {
            if (cbcCipher == null) {
                aesLock.withLock {
                    if (cbcCipher == null) {
                        cbcCipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
                    }
                }
            }
        }

        fun ctrInit(): Unit {
            if (ctrCipher == null) {
                aesLock.withLock {
                    if (ctrCipher == null) {
                        ctrCipher = Cipher.getInstance("AES/CTR/PKCS5Padding")
                    }
                }
            }
        }

        fun rfc2898Init(): Unit {
            if (rfc2898Cipher == null) {
                aesLock.withLock {
                    if (rfc2898Cipher == null) {
                        rfc2898Cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
                    }
                }
            }
        }
    }
}