package soft.common

import soft.common.DateUtil.DEFAULT_DATE_FORMAT
import soft.common.enums.GeoLocale
import java.text.SimpleDateFormat
import java.time.*
import java.time.format.DateTimeFormatter
import java.util.*

data object DateUtil {
    const val DEFAULT_DATE_FORMAT_WITH_TIMEZONE = "yyyy-MM-dd'T'HH:mm:ss"
    const val DEFAULT_DATE_FORMAT = "yyyy-MM-dd"

    val defaultZoneId: ZoneId = ZoneId.systemDefault()

    fun now(offsetMills: Long = 0): Long {
        return System.currentTimeMillis() + offsetMills
    }

    /**
     * 자주 사용하는 국가? zoneId 의 경우는 enum 으로 관리를 하여 단순 계산이 더 좋습니다.
     * - Instant 객체를 사용하는것은 계산 보다는 무겁기 때문입니다.
     */
    fun now(zoneId: ZoneId = ZoneId.systemDefault()): Long {
        return Instant.now()
            .atZone(zoneId)
            .toInstant()
            .toEpochMilli()
    }

    fun dayMillis(day: Long = 1): Long {
        return hourMills(hour = 24)  * day
    }

    fun hourMills(hour: Long = 1): Long {
        return minuteMills(minute = 60) * hour
    }

    fun minuteMills(minute: Long = 1): Long {
        return 1 * 1000 * 60L * minute
    }

    fun parseToDate(sourceDate: String, pattern: String = DEFAULT_DATE_FORMAT): Date {
        return SimpleDateFormat(pattern).parse(sourceDate)
    }

    fun parseToLocalDate(sourceDate: String, pattern: String = DEFAULT_DATE_FORMAT): LocalDate {
        return LocalDate.parse(sourceDate, DateTimeFormatter.ofPattern(pattern))
    }

    fun parseToZonedDateTime(sourceDate: String, zoneId: ZoneId, pattern: String = DEFAULT_DATE_FORMAT): ZonedDateTime {
        return parseToLocalDateTime(sourceDate, pattern).atZone(zoneId)
    }

    fun parseToLocalDateTime(sourceDate: String, pattern: String = DEFAULT_DATE_FORMAT_WITH_TIMEZONE): LocalDateTime {
        return LocalDateTime.parse(sourceDate, DateTimeFormatter.ofPattern(pattern))
    }

    fun getZoneId(zoneId: String): ZoneId = ZoneId.of(zoneId)

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

/**
 * Date 확장 함수: ZoneId 기준 ZoneOffset 반환
 */
fun Date.zoneOffset(zoneId: String): ZoneOffset {
    return this.zoneOffset(ZoneId.of(zoneId))
}

fun Date.zoneOffset(zone: ZoneId = DateUtil.defaultZoneId): ZoneOffset {
    return this.toInstant().atZone(zone).offset
}

/**
 * LocalDate 확장 함수: ZoneId 기준 ZoneOffset 반환
 * - LocalDate는 시간 정보가 없으므로 startOfDay를 기준으로 계산
 */
fun LocalDate.zoneOffset(zoneId: String): ZoneOffset {
    return this.zoneOffset(ZoneId.of(zoneId))
}

fun LocalDate.zoneOffset(zone: ZoneId = DateUtil.defaultZoneId): ZoneOffset {
    return this.atStartOfDay(zone).offset
}

/**
 * LocalDateTime 확장 함수: ZoneId 기준 ZoneOffset 반환
 */
fun LocalDateTime.zoneOffset(zoneId: String): ZoneOffset {
    return this.zoneOffset(ZoneId.of(zoneId))
}

fun LocalDateTime.zoneOffset(zone: ZoneId = DateUtil.defaultZoneId): ZoneOffset {
    return this.atZone(zone).offset
}

/**
 * Date → milliseconds
 */
fun Date.toMillis(): Long =
    this.time

/**
 * LocalDate → milliseconds
 * - 시간 정보가 없으므로 startOfDay(zone) 기준
 */
fun LocalDate.toMillis(zonOffset: ZoneOffset = ZoneOffset.UTC): Long =
    this.atStartOfDay(zonOffset).toInstant().toEpochMilli()

fun LocalDate.toMillis(zone: ZoneId = DateUtil.defaultZoneId): Long =
    this.atStartOfDay(zone).toInstant().toEpochMilli()

/**
 * LocalDateTime → milliseconds
 */
fun LocalDateTime.toMillis(zonOffset: ZoneOffset = ZoneOffset.UTC): Long =
    this.toInstant(ZoneOffset.UTC).toEpochMilli()


fun LocalDateTime.toMillis(zone: ZoneId = DateUtil.defaultZoneId): Long =
    this.atZone(zone).toInstant().toEpochMilli()

fun LocalDateTime.startOfDay(): LocalDateTime =
    this.toLocalDate().atStartOfDay()

fun LocalDateTime.endOfDay(maxTime: LocalTime = LocalTime.MAX): LocalDateTime =
    this.toLocalDate().atTime(maxTime)

fun LocalDateTime.startOfMonth(dayOfMonth: Int = 1): LocalDateTime =
    this.withDayOfMonth(dayOfMonth).toLocalDate().atStartOfDay()

fun LocalDateTime.endOfMonth(maxTime: LocalTime = LocalTime.MAX): LocalDateTime =
    this.withDayOfMonth(this.toLocalDate().lengthOfMonth()).toLocalDate().atTime(maxTime)