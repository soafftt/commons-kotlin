package soft.common

import org.junit.jupiter.api.Test
import soft.common.DateUtil.millisToDay
import java.util.concurrent.TimeUnit

class DateUtilTest {
    @Test
    fun millisToDay_returnsExpectedValue() {
        val now = System.currentTimeMillis()
        val day = TimeUnit.MILLISECONDS.toDays(now)

        println(now)
        println(day)
    }
}