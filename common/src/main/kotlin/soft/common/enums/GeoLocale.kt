package soft.common.enums

import java.util.Locale

/**
 * TODO: 주요 국가만 추가.
 *  - Locale.getAvailableLocales() 에서 직접 찾는 방법도 있음
 *  - 모듈에서는 주교 국가만 제어함.
 */
enum class GeoLocale(val locale: Locale) {
    BRAZIL(Locale.of("pt", "BR")),
    CANADA(Locale.of("en", "CA")),
    FRANCE(Locale.of("fr", "FR")),
    GERMANY(Locale.of("de", "DE")),
    JAPAN(Locale.of("ja", "JP")),
    MEXICO(Locale.of("es", "MX")),
    SOUTH_KOREA(Locale.of("ko", "KR")),
    UNITED_KINGDOM(Locale.of("en", "GB")),
    UNITED_STATES(Locale.of("en", "US")),
    ;

    companion object {
        private val defaultLocale = Locale.getDefault()
        private val countryCodeLocalMap = entries.associateBy { it.locale.country }

        fun lookup(countryCodeIosAlpha2: String) : Locale {
            return countryCodeLocalMap[countryCodeIosAlpha2.uppercase()]?.locale ?: defaultLocale
        }
    }
}