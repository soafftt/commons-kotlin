package soft.r2dbc.core.enums

import org.springframework.data.r2dbc.dialect.H2Dialect
import org.springframework.data.r2dbc.dialect.MySqlDialect
import org.springframework.data.r2dbc.dialect.PostgresDialect
import org.springframework.data.r2dbc.dialect.R2dbcDialect

enum class R2dbcDialect(val dialect: R2dbcDialect) {
    MYSQL(MySqlDialect.INSTANCE),
    POSTGRESQL(PostgresDialect.INSTANCE),
    H2(H2Dialect.INSTANCE);
}