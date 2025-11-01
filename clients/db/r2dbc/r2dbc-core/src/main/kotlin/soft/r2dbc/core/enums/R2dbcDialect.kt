package soft.r2dbc.core.enums

import org.jetbrains.exposed.v1.core.vendors.DatabaseDialect
import org.springframework.data.r2dbc.dialect.MySqlDialect
import org.springframework.data.r2dbc.dialect.PostgresDialect
import org.springframework.data.r2dbc.dialect.R2dbcDialect
import org.jetbrains.exposed.v1.core.vendors.MysqlDialect as EXPOSED_MySqlDialect
import org.jetbrains.exposed.v1.core.vendors.PostgreSQLDialect as EXPOSED_PostgreSQLDialect

enum class R2dbcDialect(val springDialect: R2dbcDialect, val exposedDialect: DatabaseDialect) {
    MYSQL(MySqlDialect.INSTANCE, EXPOSED_MySqlDialect()),
    POSTGRESQL(PostgresDialect.INSTANCE, EXPOSED_PostgreSQLDialect()),
}