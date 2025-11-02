package soft.r2dbc.write

import io.r2dbc.spi.ConnectionFactory
import org.jetbrains.exposed.v1.r2dbc.R2dbcDatabase
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import soft.r2dbc.core.config.MySqlDnsConfig
import soft.r2dbc.core.config.MySqlDnsConfig.MysqlDnsResolver
import soft.r2dbc.core.enums.R2dbcImplementation
import soft.r2dbc.core.makeConnectionFactory
import soft.r2dbc.core.makeExposedR2dbc
import soft.r2dbc.core.makePool
import soft.r2dbc.core.properties.R2dbcConfigurationProperties
import soft.r2dbc.core.properties.mysql.MySqlTcpProperties
import soft.r2dbc.write.properties.WriterDataSourceProperties
import soft.r2dbc.write.properties.WriterPoolProperties

@EnableConfigurationProperties(
    value = [
        R2dbcConfigurationProperties::class,
        WriterDataSourceProperties::class,
        WriterPoolProperties::class,
        MySqlTcpProperties::class,
        MySqlDnsConfig::class
    ]
)
@Configuration
@ConditionalOnExpression("'$(r2dbc.database.writer:_MISS_)' != '_MISS_'")
@ConditionalOnProperty(value = ["r2dbc.datasource.writer"])
class WriterExposedConnectionFactoryConfig(
    private val r2DbcConfigurationProperties: R2dbcConfigurationProperties,
    private val writerDataSourceProperties: WriterDataSourceProperties,
    private val writerPoolProperties: WriterPoolProperties,
    @param:Autowired(required = false) private val mySqlTcpProperties: MySqlTcpProperties? = null,
    @param:Autowired(required = false) private val mysqlDnsResolver: MysqlDnsResolver? = null
) {
    @Primary
    @Bean
    fun r2dbcDatabase(): R2dbcDatabase {
        return writerDataSourceProperties
            .makeConnectionFactory(mySqlTcpProperties, mysqlDnsResolver)
            .makePool(writerPoolProperties)
            .let { connectionFactory ->
                makeExposedR2dbc(connectionFactory, r2DbcConfigurationProperties.dialect)
            }
    }
}