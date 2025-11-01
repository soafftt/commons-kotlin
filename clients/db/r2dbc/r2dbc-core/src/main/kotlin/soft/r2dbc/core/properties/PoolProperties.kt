package soft.r2dbc.core.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("r2dbc.pool")
data class PoolProperties(
    var initialSize: Int = 5,
    var maxSize: Int = 30,
    var minIdle: Int = 2,
    var maxIdleTime: Long = 20000,
    var maxLifeTime: Long = 60000,
    var maxCreateConnectionTime: Long = 30000,
    var validationTimeout: Long = 5000,
    var validationQuery: String = "SELECT 1",
    var acquireRetry: Int = 3,
    var warmup: Boolean = false,
    var maxAcquireTime: Long = 5000,
    var evictionInterval: Long? = null,
)