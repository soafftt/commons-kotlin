package soft.commons.util

import soft.commons.extension.CommonExtension.Companion.toBase64Array
import java.security.*
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class RsaKeyPairUtil {
    companion object {
        private val rsaLock = ReentrantLock()
        private val keyPairLock = ReentrantLock()

        private var keyFactory: KeyFactory? = null
        private var defaultKeyPair: KeyPair? = null
        private var keyPairGenerator: KeyPairGenerator? = null

        private fun keyFactoryInit(): Unit {
            if (keyFactory == null) {
                rsaLock.withLock {
                    if (keyFactory == null) {
                        keyFactory = KeyFactory.getInstance("RSA");
                    }
                }
            }
        }

        private fun keyPairGeneratorInit() {
            if (keyPairGenerator == null) {
                keyPairLock.withLock {
                    if (keyPairGenerator == null) {
                        keyPairGenerator = KeyPairGenerator.getInstance("RSA").also {
                            it.initialize(2048)
                        }
                    }
                }
            }
        }

        fun makePublicKey(publicKeyBytes: ByteArray): PublicKey {
            keyFactoryInit()

            return keyFactory!!.generatePublic(X509EncodedKeySpec(publicKeyBytes));
        }

        fun makePrivateKey(privateKeyBytes: ByteArray): PrivateKey {
            keyFactoryInit()

            return keyFactory!!.generatePrivate(PKCS8EncodedKeySpec(privateKeyBytes));
        }

        fun getDefaultKeyPair(): KeyPair {
            if (defaultKeyPair == null) {
                rsaLock.withLock {
                    if (defaultKeyPair == null) {
                        keyFactoryInit()

                        val publicKeySpec =
                            X509EncodedKeySpec("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkXOIKgHFGJFjNenq3vlF8FkBTgq5BDL/G1AjeI9ViKvjgkalpncwLSHGuq3UNLd8cqQ8fM4peEMjMm0gg6VhEqxd3yXqK1LQNswJggtAV0hNaD4a9OymKMwEPhMQQU8ykDUGxBTOdVcqNtWU+puhcHaTNhoQOE8jokYHN/e8VQrn4yJoF0KayF+gsr3ov0p1aQMJP62hhMz1Tx5VXCJYU2c6so2Yyu/174v4IeZzuUXriyzyRBHI1Fc45ARL+jLEIgR5Vw2fvJ47VmCKmOd43yPZgt8753qkk1TSEm4pvobFYKqlol843KYnb0RYNAuPBzwlwbRAHDW3B6vylEyYuwIDAQAB".toBase64Array());
                        val privateKeySpec =
                            PKCS8EncodedKeySpec(
                                "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCRc4gqAcUYkWM16ere+UXwWQFOCrkEMv8bUCN4j1WIq+OCRqWmdzAtIca6rdQ0t3xypDx8zil4QyMybSCDpWESrF3fJeorUtA2zAmCC0BXSE1oPhr07KYozAQ+ExBBTzKQNQbEFM51Vyo21ZT6m6FwdpM2GhA4TyOiRgc397xVCufjImgXQprIX6Cyvei/SnVpAwk/raGEzPVPHlVcIlhTZzqyjZjK7/Xvi/gh5nO5ReuLLPJEEcjUVzjkBEv6MsQiBHlXDZ+8njtWYIqY53jfI9mC3zvneqSTVNISbim+hsVgqqWiXzjcpidvRFg0C48HPCXBtEAcNbcHq/KUTJi7AgMBAAECggEAOaX3xuZyrt0Y3Ep9G6jiznMIcF0RnZd0wueNV4A/3255Oq4zg3nj709ey6iP3eEHgwyTKMgxaYf6kEbuRx8qDVOh1Qra+BbXjZBrCE7bTnzKqVFML90Hsk3CNLQrkicInF1X9Clm9tz4T0lxxa4fW0qz6BKGcTr0naFxxP38eBvAdDiS+NP31G+6d09/hqA2hJESZJZH2x7NGXT+bangIAra8VrBd2fsIjhe/Z3NV+dSYmGTH7sRAF5n0fnIcMUZofeKMAquTwvFWWULA3mhkTlHmVZJN+mavTMvklo74B5+VoFAwLb/cMBA4yjvBNE3O23JGlEtOcN2cv5d2CmgkQKBgQDypUIlTurntVKHD0zK/ARM/NrXxQaAOtHbywmWPBCEYeoxqbLSb3Ple8CpepDBw2wuGzIB1OUZpAC9PvOqsuZEl59L6BdR2JURixLEjHBD7MsrpAsuXhqiPjP1nfYWVTvRG4wzeQT+ep9Qwj20yyj/QmlNM6Dx7dQSVDnnR0FskwKBgQCZdNw85RKEL+wz+OkFfzu9NIu0utsLjamwo78LC+v284ngmElbVV7MLlOjWmJPjgZX/xCOtnLW34cKMV+l+WUD2g58Xw5ydTVco0GquUmiS9tTwRrZqht5+1gkxTBGes13wQShqn5EwOZoQlmwoOl1xe/nqaOMC6nBQhFw26ZkOQKBgDCGH+46E/v2ZOShiKfnMiz7PAB+ZEhset9LgUVMCbmPozf/ScWPiEvSLbs2yZAWNqIZyixXmOFBzOwLlMsEL8xzzeVuKouxlk4F0+D+fMz4o8C8c4f4RbdAXT+3MSlSLj4pFiaNAxSpDQcncROgtTgm3cwUkREQkKKBuXqo40qFAoGAQV2x4o6BEKWJK6o/OAQ2YiXbzKQ7YfR577AQVJhDbvHWLUExHiKDOt4Q6mg5sEGDGkCfwOqeiEC2uPTHFV/iU32y5e9nrAGZNVilRB+g6ez+A/MhiM4Y3iDeLut/4MW2d+hUHLkPCCJTAt4gbkhcqboisr9j1uew641E+JnXiqECgYEA18lm9kwdcW8bRezznZ9z9GkTwzrUPSbaJn01Ej+zASnd+xJu3LDQMc3y7eGorHOZS5zkkOv3xcXACBvF94V0DbldurJvSSxNhDBrLTLCfL3GwFuS7LmMDUF6+vv7Pkg3IVica8iQ6Ng4PJgEm7faVzJH1eMEazkcVpvpS2ibWHg=".toBase64Array()
                            );

                        defaultKeyPair = KeyPair(
                            keyFactory!!.generatePublic(publicKeySpec),
                            keyFactory!!.generatePrivate(privateKeySpec)
                        )
                    }
                }
            }
            return defaultKeyPair!!
        }

        fun getRandomKeyPair(): KeyPair = try {
            keyPairGeneratorInit()

            this.keyPairGenerator!!.generateKeyPair()
        } catch (ex: Exception) {
            getDefaultKeyPair()
        }

        private fun makeKeyPair(publicKeyBytes: ByteArray, privateKeyBytes: ByteArray): KeyPair {
            keyFactoryInit()

            val publicKeySpec = X509EncodedKeySpec(publicKeyBytes);
            val privateKeySpec = PKCS8EncodedKeySpec(privateKeyBytes);

            return KeyPair(
                keyFactory!!.generatePublic(publicKeySpec),
                keyFactory!!.generatePrivate(privateKeySpec)
            );
        }
    }
}