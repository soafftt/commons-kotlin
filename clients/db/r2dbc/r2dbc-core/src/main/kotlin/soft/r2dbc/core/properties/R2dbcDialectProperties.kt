package soft.r2dbc.core.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import soft.r2dbc.core.enums.R2dbcDialect

@ConfigurationProperties("r2dbc.dialect")
data class R2dbcDialectProperties(
    val dialect: R2dbcDialect,
)