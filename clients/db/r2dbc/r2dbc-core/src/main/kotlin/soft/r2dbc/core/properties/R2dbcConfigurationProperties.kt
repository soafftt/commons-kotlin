package soft.r2dbc.core.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import soft.r2dbc.core.enums.R2dbcDialect
import soft.r2dbc.core.enums.R2dbcImplementation

@ConfigurationProperties("r2dbc")
data class R2dbcConfigurationProperties(
    val dialect: R2dbcDialect,
    val implementation: R2dbcImplementation
)