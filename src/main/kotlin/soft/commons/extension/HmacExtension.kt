package soft.commons.extension

import java.util.concurrent.locks.ReentrantLock
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import kotlin.concurrent.withLock

class HmacExtension {
    companion object {
        private val mac256Lock = ReentrantLock()
        private var mac256: Mac? = null

        private fun initMac256(): Unit {
            if(mac256 == null) {
                mac256Lock.withLock {
                    if(mac256 == null) {
                        mac256 = Mac.getInstance("HmacSHA256");
                    }
                }
            }
        }

        fun String.encryptToMac256(key: String): ByteArray = try {
            initMac256()

            val secretKey = SecretKeySpec(key.toByteArray(Charsets.UTF_8), "HmacSHA256")

            mac256!!.init(secretKey)
            mac256!!.doFinal(toByteArray(Charsets.UTF_8))
        } catch (e: Exception) {
            toByteArray()
        }
    }
}