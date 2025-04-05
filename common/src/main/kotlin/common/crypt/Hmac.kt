package common.crypt

import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.locks.ReentrantLock
import javax.crypto.Mac
import kotlin.concurrent.withLock

object Hmac {
    enum class Algorithm {
        HmacMD5,
        HmacSHA1,
        HmacSHA224,
        HmacSHA384,
        HmacSHA512
    }

    private val hMacLock = ReentrantLock()
    private val hmacAlgorithmMap = mutableMapOf<Algorithm, Mac>()

    private val hmacAlgorithmMapLoopCount = AtomicInteger(0)

    init {
        if (hmacAlgorithmMap.size != Algorithm.entries.size) {
            hMacLock.withLock {
                if (hmacAlgorithmMap.size != Algorithm.entries.size) {
                    Algorithm.entries.forEach { entry ->
                        if (!hmacAlgorithmMap.containsKey(entry)) {
                            hmacAlgorithmMap[entry] = Mac.getInstance(entry.name)
                            hmacAlgorithmMapLoopCount.incrementAndGet()
                        }
                    }
                }
            }
        }
    }

    val instanceOf: (Algorithm) -> Mac = {
        hmacAlgorithmMap[it] ?: throw RuntimeException("Unsupported algorithm: $it")
    }
}