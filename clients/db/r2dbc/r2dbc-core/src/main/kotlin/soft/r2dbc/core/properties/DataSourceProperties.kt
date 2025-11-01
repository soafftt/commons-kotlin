package soft.r2dbc.core.properties

import soft.r2dbc.core.enums.ConnectionFactoryType

open class DataSourceProperties {
    val host: String = ""
    val port: Int = 0
    val database: String = ""
    val username: String = ""
    val password: String = ""
    val ssl: Boolean = false
    val factoryType: ConnectionFactoryType = ConnectionFactoryType.SPI
}