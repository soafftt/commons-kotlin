package soft.commons.extenstions

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import soft.commons.extenstions.JsonExtension.Companion.deserializeJson
import soft.commons.extenstions.JsonExtension.Companion.deserializeJsonArray
import soft.commons.extenstions.JsonExtension.Companion.toJson

class JsonExtensionTest {
    data class ExpectedJson @JsonCreator constructor(
        @JsonProperty("idx")
        val idx: Int,

        @JsonProperty("idxArray")
        val idxArray: Array<ExpectedInnerArray>
    ) {
        data class ExpectedInnerArray @JsonCreator constructor(
            @JsonProperty("idx")
            val idx: Int
        )

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as ExpectedJson

            if (idx != other.idx) return false
            return idxArray.contentEquals(other.idxArray)
        }

        override fun hashCode(): Int {
            var result = idx
            result = 31 * result + idxArray.contentHashCode()
            return result
        }
    }

    @Test
    @DisplayName("json 문자열을 object 로 변환한다.")
    fun `jsonString to object convert success`() {
        val jsonString = "{\"idx\": 1, \"idxArray\": [{\"idx\": 1}]}"

        val expectedJson: ExpectedJson? = jsonString.deserializeJson(ExpectedJson::class.java, ignoreException = true)

        Assertions.assertNotNull(expectedJson)
        Assertions.assertEquals(1, expectedJson!!.idx)
        Assertions.assertEquals(1, expectedJson.idxArray.size)
        Assertions.assertEquals(1, expectedJson.idxArray[0].idx)
    }

    @Test
    @DisplayName("잘못된 json 문자열을 object 변환을 시도 하지만, ignoreException = true 로 인하여 null 이 return 된다.")
    fun `invalid jsonString to object when ignoreException = true return null`() {
        val jsonString = "abcdef"

        val expectedJson: ExpectedJson? = jsonString.deserializeJson(ExpectedJson::class.java, ignoreException = true)

        Assertions.assertNull(expectedJson)
    }

    @Test
    @DisplayName("잘못된 json 문자열을 object 변환을 시도 하지만, ignoreException = false 로 인하여 RuntimeException 이 throw 된다.")
    fun `invalid jsonString to object when ignoreException = false throw RuntimeException`() {
        val jsonString = "abcdef"
        val runTimeException: RuntimeException = Assertions.assertThrows(RuntimeException::class.java) {
            jsonString.deserializeJson(ExpectedJson::class.java, ignoreException = false)
        }
    }

    @Test
    @DisplayName("json 배열을 object로 변환한다")
    fun `jsonArrayString to object success`() {
        val jsonString = "[1, 2, 3, 4]"

        val objList: List<Int> = jsonString.deserializeJsonArray(Int::class.java, ignoreException = false)

        Assertions.assertEquals(4, objList.size)

        Assertions.assertEquals(1, objList[0])
        Assertions.assertEquals(2, objList[1])
        Assertions.assertEquals(3, objList[2])
        Assertions.assertEquals(4, objList[3])
    }

    @Test
    @DisplayName("invalid json 배열 문자를 object 로 변환시도 하지만 ignoreException = true 로 인하여 emptyList 가 return 된다")
    fun `invalid jsonArrayString then ignoreException = true return emptyList`() {
        val jsonString = "1,2,3,4"

        val objList: List<Int> = jsonString.deserializeJsonArray(Int::class.java, ignoreException = true)

        Assertions.assertTrue(objList.isEmpty())
    }

    @Test
    @DisplayName("invalid json 배열 문자를 object 로 변환시도 하지만 ignoreException = false 로 인하여 RuntimeException 이 throw 된다.")
    fun `invalid jsonArrayString then ignoreException = false throw RuntimeException`() {
        val jsonString = "1,2,3,4"

        Assertions.assertThrows(RuntimeException::class.java) {
            jsonString.deserializeJsonArray(Int::class.java, ignoreException = false)
        }
    }

    @Test
    @DisplayName("object 를 json 문자열로 변환한다.")
    fun `object to jsonString success`() {
        val expectedJson: ExpectedJson = ExpectedJson(
            idx = 1,
            idxArray = arrayOf(ExpectedJson.ExpectedInnerArray(1))
        )
        val expectedJsonString = "{\"idx\":1,\"idxArray\":[{\"idx\":1}]}"

        val jsonString = expectedJson.toJson()

        Assertions.assertEquals(expectedJsonString, jsonString)
    }
}