package soft.common

import soft.common.DateUtil.DEFAULT_DATE_FORMAT
import soft.common.enums.GeoLocale
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

data object DateUtil {
    const val DEFAULT_DATE_FORMAT_WITH_TIMEZONE = "yyyy-MM-dd'T'HH:mm:ss"
    const val DEFAULT_DATE_FORMAT = "yyyy-MM-dd"

    fun now(offsetMills: Long = 0): Long {
        return System.currentTimeMillis() + offsetMills
    }

    /**
     * 자주 사용하는 국가? zoneId 의 경우는 enum 으로 관리를 하여 단순 계산이 더 좋습니다.
     * - Instant 객체를 사용하는것은 계산 보다는 무겁기 때문입니다.
     */
    fun now(zoneId: ZoneId? = null): Long {
        val now = this.now(offsetMills = 0)
        return if (zoneId == null) {
            now
        } else {
            Instant.ofEpochMilli(now)
                .atZone(zoneId)
                .toInstant()
                .toEpochMilli()
        }
    }

    fun dayMillis(day: Long = 1): Long {
        return hourMills(hour = 24)  * day
    }

    fun hourMills(hour: Long = 1): Long {
        val offsetHour = 1 * 1000 * 60 * 60L
        return minuteMills(minute = 60) * hour
    }

    fun minuteMills(minute: Long = 1): Long {
        val offsetHour = 1 * 1000 * 60L
        return offsetHour * minute
    }
}

fun Date.format(countryCodeIsoAlpha2: String, format: String = DEFAULT_DATE_FORMAT): String {
    val locale = GeoLocale.lookup(countryCodeIsoAlpha2)
    return this.format(locale, format)
}

fun Date.format(getLocale: GeoLocale, format: String = DEFAULT_DATE_FORMAT): String {
    return this.format(getLocale.locale, format)
}

fun Date.format(locale: Locale, format: String = DEFAULT_DATE_FORMAT): String {
    val dateFormat = SimpleDateFormat(format, locale)
    return dateFormat.format(this)
}

fun LocalDate.format(countryCodeIsoAlpha2: String, format: String = DEFAULT_DATE_FORMAT): String {
    val locale = GeoLocale.lookup(countryCodeIsoAlpha2)
    return this.format(locale, format)
}

fun LocalDate.format(getLocale: GeoLocale, format: String = DEFAULT_DATE_FORMAT): String {
    return this.format(getLocale.locale, format)
}

fun LocalDate.format(locale: Locale, format: String = DEFAULT_DATE_FORMAT): String {
    val dateFormat = DateTimeFormatter.ofPattern(format, locale)
    return this.format(dateFormat)
}

fun LocalDateTime.format(countryCodeIsoAlpha2: String, format: String = DEFAULT_DATE_FORMAT): String {
    val locale = GeoLocale.lookup(countryCodeIsoAlpha2)
    return this.format(locale, format)
}

fun LocalDateTime.format(getLocale: GeoLocale, format: String = DEFAULT_DATE_FORMAT): String {
    return this.format(getLocale.locale, format)
}

fun LocalDateTime.format(locale: Locale, format: String = DEFAULT_DATE_FORMAT): String {
    val dateFormat = DateTimeFormatter.ofPattern(format, locale)
    return this.format(dateFormat)
}

fun LocalDateTime.format(dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME): String =
    dateTimeFormatter.format(this)


