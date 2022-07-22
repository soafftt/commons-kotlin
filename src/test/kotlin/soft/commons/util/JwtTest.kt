package soft.commons.util

import com.fasterxml.jackson.annotation.JsonCreator
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import soft.commons.extension.CommonExtension.Companion.toHexString
import soft.commons.extension.JsonExtension.Companion.decodeJwtToObject
import soft.commons.extension.JsonExtension.Companion.encodeJwt
import soft.commons.jwt.JwtBase
import java.security.MessageDigest
import javax.crypto.Mac

class JwtTest {

    @Test
    fun test_jwtEncode_success() = runBlocking {
        solution(intArrayOf(1, 5, 2, 6, 3, 7, 4), arrayOf(intArrayOf(2, 5, 3)))
    }

    fun solution(array: IntArray, commands: Array<IntArray>): IntArray {
        var answer = intArrayOf()
        var index = 0;

        for(command in commands) {
            val newArray = array.sliceArray(command[0] until command[1]).sorted()
            println(newArray)
            answer[index] = newArray[command[2] - 1]
            index += 1
        }

        return answer
    }

    data class Jwt @JsonCreator constructor (
        val indexSN: Long
    ) : JwtBase() {
        override fun getClaims(): Map<String, Any?> {
            return mapOf<String, Any?>(
                "indexSN" to indexSN
            )
        }
    }
}