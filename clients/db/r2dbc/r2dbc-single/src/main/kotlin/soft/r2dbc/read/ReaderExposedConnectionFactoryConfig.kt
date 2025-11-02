package soft.r2dbc.read

import org.jetbrains.exposed.v1.r2dbc.R2dbcDatabase
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import soft.r2dbc.core.config.MySqlDnsConfig
import soft.r2dbc.core.config.MySqlDnsConfig.MysqlDnsResolver
import soft.r2dbc.core.enums.R2dbcImplementation
import soft.r2dbc.core.makeConnectionFactory
import soft.r2dbc.core.makeExposedR2dbc
import soft.r2dbc.core.makePool
import soft.r2dbc.core.properties.R2dbcConfigurationProperties
import soft.r2dbc.core.properties.mysql.MySqlTcpProperties
import soft.r2dbc.read.properties.ReaderDataSourceProperties
import soft.r2dbc.read.properties.ReaderPoolProperties

@EnableConfigurationProperties(
    value = [
        R2dbcConfigurationProperties::class,
        ReaderDataSourceProperties::class,
        ReaderPoolProperties::class,
        MySqlTcpProperties::class,
        MySqlDnsConfig::class
    ]
)
@Configuration
@ConditionalOnExpression("'$(r2dbc.database.reader:_MISS_)' != '_MISS_'")
@ConditionalOnProperty(value = ["r2dbc.implementation"], havingValue = R2dbcImplementation.USE_EXPOSED)
class ReaderExposedConnectionFactoryConfig(
    private val r2DbcConfigurationProperties: R2dbcConfigurationProperties,
    private val readerDataSourceProperties: ReaderDataSourceProperties,
    private val readerPoolProperties: ReaderPoolProperties,
    @param:Autowired(required = false) private val mySqlTcpProperties: MySqlTcpProperties? = null,
    @param:Autowired(required = false) private val mysqlDnsResolver: MysqlDnsResolver? = null
) {
    @Bean("readerR2dbcDatabase")
    fun makeReaderR2dbcDatabase(): R2dbcDatabase {
        val connectionFactory = readerDataSourceProperties
            .makeConnectionFactory(mySqlTcpProperties, mysqlDnsResolver)
            .makePool(readerPoolProperties)

        return makeExposedR2dbc(connectionFactory, r2DbcConfigurationProperties.dialect)
    }
}