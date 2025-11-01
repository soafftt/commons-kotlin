package soft.r2dbc.core.properties

open class PoolProperties(
    val initialSize: Int = 5,
    val maxSize: Int = 30,
    val minIdle: Int = 2,
    val maxIdleTime: Long = 20000,
    val maxLifeTime: Long = 60000,
    val maxCreateConnectionTime: Long = 30000,
    val validationTimeout: Long = 5000,
    val validationQuery: String = "SELECT 1",
    val acquireRetry: Int = 3,
    val warmup: Boolean = false,
    val maxAcquireTime: Long = 5000,
    val evictionInterval: Long? = null,
)