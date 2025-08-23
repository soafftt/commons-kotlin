package soft.common.enums

import java.time.ZoneId

/**
 * 나름의 주요 국가. 없으면 찾아서 하게 만들어주기
 */
enum class GeoTimezone(val zoneId: ZoneId) {
    ASIA_TOKYO(ZoneId.of("Asia/Tokyo")),          // 일본
    EUROPE_BERLIN(ZoneId.of("Europe/Berlin")),    // 독일
    EUROPE_PARIS(ZoneId.of("Europe/Paris")),      // 프랑스
    EUROPE_LONDON(ZoneId.of("Europe/London")),    // 영국
    AMERICA_NEW_YORK(ZoneId.of("America/New_York")), // 미국
    AMERICA_TORONTO(ZoneId.of("America/Toronto")),  // 캐나다
    EUROPE_ROME(ZoneId.of("Europe/Rome")),        // 이탈리아
    EUROPE_BRUSSELS(ZoneId.of("Europe/Brussels")),// 벨기에
    EUROPE_STOCKHOLM(ZoneId.of("Europe/Stockholm")), // 스웨덴
    EUROPE_ZURICH(ZoneId.of("Europe/Zurich")), // 스위스

    ;

    companion object {
        private val timeZoneMap =
            entries.associateBy {
                it.zoneId.toString()
            }

        fun lookup(zoneId: String): ZoneId =
            timeZoneMap[zoneId]?.zoneId ?: ZoneId.of(zoneId)
    }
}