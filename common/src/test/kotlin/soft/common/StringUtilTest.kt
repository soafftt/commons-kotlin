package soft.common

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class StringUtilTest {
    @Test
    fun testRandomString() {
        val result = mutableListOf<String>()
        for (i in 1..1000) {
            result.add(randomString(length = 10))
        }

        Assertions.assertEquals(result.size, result.distinct().size)
    }

    @Test
    fun `Seed 가 같을때 동일한 문자열이 나옴`() {
        val result = mutableListOf<String>()
        for (i in 1..1000) {
            result.add(randomString(length = 10, seed = 100000))
        }

        Assertions.assertEquals(1, result.distinct().size)
    }

    @Test
    fun `Seed 를 다르게 입력하여 다른 문자열이 나오도록 함`() {
        val result = mutableListOf<String>()
        for (i in 1..1000) {
            result.add(randomString(length = 10, seed = i.toLong()))
        }

        Assertions.assertEquals(result.size, result.distinct().size)
    }
}