package soft.r2dbc

import org.springframework.boot.context.properties.ConfigurationProperties
import soft.r2dbc.core.properties.PoolProperties

@ConfigurationProperties(prefix = "r2dbc.pool")
class R2dbcPoolProperties: PoolProperties()