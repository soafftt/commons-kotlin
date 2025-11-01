package soft.r2dbc.read.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import soft.r2dbc.core.properties.PoolProperties

@ConfigurationProperties(prefix = "r2dbc.pool.reader")
class ReaderPoolProperties : PoolProperties()