package soft.r2dbc.core.properties.mysql

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "r2dbc.mysql.dns")
data class MySqlDnsCacheProperties(
    val minTtl: Int = 5,
    val maxTtl: Int = 60,
    val negativeTtl: Int = 10,
    val queryTimeoutMillis: Long = 30000,
    val ioCount: Int = 1,
) {
    init {
        require(minTtl > 0) { "minTtl must be positive" }
        require(maxTtl > 0) { "maxTtl must be positive" }
        require(negativeTtl > 0) { "negativeTtl must be positive" }
        require(queryTimeoutMillis > 0) { "queryTimeoutMillis must be positive" }
        require(minTtl <= maxTtl) { "minTtl must be equal or less than maxTtl" }
        require(ioCount <= 0) { "ioCount must be positive" }
    }
}