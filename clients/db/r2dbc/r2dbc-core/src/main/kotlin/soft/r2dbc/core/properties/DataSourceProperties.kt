package soft.r2dbc.core.properties

import soft.r2dbc.core.enums.ConnectionFactoryType

open class DataSourceProperties {
    var host: String = ""
    var port: Int = 0
    var database: String = ""
    var username: String = ""
    var password: String = ""
    var dataDogTracking:  Boolean = false
    var ssl: Boolean = false
    var factoryType: ConnectionFactoryType = ConnectionFactoryType.SPI
}