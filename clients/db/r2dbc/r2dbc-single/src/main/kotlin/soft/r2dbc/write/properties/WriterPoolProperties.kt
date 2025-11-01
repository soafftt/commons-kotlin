package soft.r2dbc.write.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import soft.r2dbc.core.properties.PoolProperties

@ConfigurationProperties(prefix = "r2dbc.pool.writer")
class WriterPoolProperties : PoolProperties()