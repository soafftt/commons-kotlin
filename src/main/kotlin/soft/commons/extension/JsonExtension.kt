package soft.commons.extension

import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import soft.commons.crypto.Rfc2898Driver
import soft.commons.enums.CommonErrorCode
import soft.commons.exeption.CommonErrorException
import soft.commons.extension.AesExtension.Companion.decryptToAesCbcRfc289
import soft.commons.extension.AesExtension.Companion.encryptToAesCbcRfc289
import soft.commons.extension.JsonExtension.Companion.decodeJson
import soft.commons.extension.JsonExtension.Companion.encodeJwt
import soft.commons.jwt.CipherPayload
import soft.commons.jwt.JwtBase
import java.io.IOException

class JsonExtension {
    companion object {
        private const val JSON_INITIALIZE = "{}"

        private val jsonFactory = JsonFactory()
        private val objectMapper = ObjectMapper(jsonFactory)

        fun <T> String.decodeJson(cls: Class<T>, ignoreException: Boolean = false): T? =
            when (ignoreException) {
                true -> runCatching { objectMapper.readValue(this, cls) }.getOrNull()
                else -> objectMapper.readValue(this, cls)
            }

        fun String.decodeJson(json: String): JsonNode? = try {
            objectMapper.readTree(json)
        } catch (exception: Exception) {
            null
        }

        fun <T> JsonNode.decodeJson(cls: Class<T>, ignoreException: Boolean = true): T? = when (ignoreException) {
            true -> try {
                objectMapper.treeToValue(this, cls)
            } catch (exception: Exception) {
                null
            }
            else -> objectMapper.treeToValue(this, cls)
        }

        fun <T> String.decodeJsonArray(cls: Class<T>): List<T>? = try {
            objectMapper.readValue(this, objectMapper.typeFactory.constructCollectionType(List::class.java, cls))
        } catch (e: IOException) {
            null
        }

        fun Any.encodeJson(): String = try {
            objectMapper.writeValueAsString(this)
        } catch (ex: JsonProcessingException) {
            JSON_INITIALIZE
        }

        fun JwtBase.encodeJwt(key: String): String = encodeJwt(key, getClaims())

        fun HashMap<String, Any?>.encodeJwt(key: String): String = encodeJwt(key, this)

        private fun encodeJwt(key: String, claims: Map<String, Any?>): String = try {
            Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(key.toByteArray(Charsets.UTF_8)))
                .setClaims(claims)
                .compact() ?: ""
        } catch (ex: Exception) {
            throw CommonErrorException(CommonErrorCode.JWT_ENCODE.code, throwable = ex)
        }

        fun String.decodeJwtToMap(key: String): Map<String, Any?> = decodeJwtToClams(this, key)

        fun <T> String.decodeJwtToObject(key: String, cls: Class<T>): T = try {
            val map = this.decodeJwtToMap(key)
            val json = map.encodeJson()

            json.decodeJson(cls) ?: throw CommonErrorException(CommonErrorCode.JWT_DECODE.code)
        } catch (ex: CommonErrorException) {
            throw ex
        } catch (ex: Exception) {
            throw CommonErrorException(CommonErrorCode.JWT_DECODE.code, throwable = ex)
        }

        private fun decodeJwtToClams(token: String, key: String): Map<String, Any?> = try {
            Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(key.toByteArray(Charsets.UTF_8)))
                .build()
                .parseClaimsJws(token)
                .body;
        } catch (ex: Exception) {
            throw CommonErrorException(CommonErrorCode.JWT_DECODE.code, throwable = ex)
        }

        fun JwtBase.encodeJwtWithPayloadAesCrypt(jwtKey: String, aesPwd: String, aesSalt: String): String = try {
            this.getClaims().encodeJson()
                .encryptToAesCbcRfc289(aesPwd, aesSalt)
                .let {
                    hashMapOf<String, Any?>(Pair("name", it)).encodeJwt(jwtKey)
                }
        } catch (ex: CommonErrorException) {
            throw ex
        } catch (ex: Exception) {
            throw CommonErrorException(CommonErrorCode.JWT_ENCODE.code, throwable = ex)
        }

        fun <T> String.decodeJwtWithPayloadAesCrypt(jwtKey: String, aesPwd: String, aesSalt: String, cls: Class<T>): T = try {
            this.decodeJwtToObject(jwtKey, CipherPayload::class.java)
                .run {
                    cipherText.decryptToAesCbcRfc289(aesPwd, aesSalt)
                }.decodeJson(cls)!!
        } catch (ex: CommonErrorException) {
            throw ex
        } catch (ex: Exception) {
            throw CommonErrorException(CommonErrorCode.JWT_DECODE.code, throwable = ex)
        }
    }
}