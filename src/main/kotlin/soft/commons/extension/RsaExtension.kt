package soft.commons.extension

import soft.commons.extension.CommonExtension.Companion.toBase64Array
import soft.commons.extension.CommonExtension.Companion.toBase64String
import soft.commons.util.RsaKeyPairUtil
import java.security.PrivateKey
import java.security.PublicKey
import java.util.concurrent.locks.ReentrantLock
import javax.crypto.Cipher
import kotlin.concurrent.withLock

class RsaExtension {
    private class RsaCipher {
        companion object {
            private val rsaLock = ReentrantLock()

            var cipher: Cipher? = null
                private set

            fun init() : Unit {
                if(cipher == null) {
                    rsaLock.withLock {
                        if(cipher == null) {
                            cipher = Cipher.getInstance("RSA")
                        }
                    }
                }
            }
        }
    }

    companion object {
        fun String.encryptToRsa(key: PublicKey): String = try {
            RsaCipher.init()
            RsaCipher.cipher!!.init(Cipher.ENCRYPT_MODE, key)

            RsaCipher.cipher!!.doFinal(this.toByteArray()).toBase64String()
        } catch(ex: Exception) {
            this
        }

        fun String.decryptToRsa(key: PrivateKey): String = try {
            RsaCipher.init()
            RsaCipher.cipher!!.init(Cipher.DECRYPT_MODE, key)

            String(RsaCipher.cipher!!.doFinal(this.toBase64Array()))
        } catch(ex: Exception) {
            this
        }

        fun ByteArray.toPublicKey(): PublicKey = RsaKeyPairUtil.makePublicKey(this)
        fun ByteArray.toPrivateKey(): PrivateKey = RsaKeyPairUtil.makePrivateKey(this)
    }
}