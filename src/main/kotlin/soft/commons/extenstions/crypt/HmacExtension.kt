package soft.commons.extenstions.crypt

import soft.commons.HMAC_SHA_256_ALGORITHM
import soft.commons.crypto.Hmac
import javax.crypto.spec.SecretKeySpec

class HmacExtension {
    companion object {
        fun String.encryptToMac256(key: String): ByteArray = try {
            Hmac.initHmac256()

            val secretKey = SecretKeySpec(key.toByteArray(Charsets.UTF_8), HMAC_SHA_256_ALGORITHM)

            Hmac.mac256!!.init(secretKey)
            Hmac.mac256!!.doFinal(toByteArray(Charsets.UTF_8))
        } catch (e: Exception) {
            toByteArray()
        }
    }
}