package soft.r2dbc.write

import org.jetbrains.exposed.v1.r2dbc.R2dbcDatabase
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import soft.r2dbc.R2dbcPoolProperties
import soft.r2dbc.core.config.MySqlDnsConfig
import soft.r2dbc.core.config.MySqlDnsConfig.MysqlDnsResolver
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
        R2dbcPoolProperties::class,
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
    private val writerPoolProperties: WriterPoolProperties? = null,
    private val r2dbcPoolProperties: R2dbcPoolProperties? = null,
    @param:Autowired(required = false) private val mySqlTcpProperties: MySqlTcpProperties? = null,
    @param:Autowired(required = false) private val mysqlDnsResolver: MysqlDnsResolver? = null
) {
    init {
        require(writerPoolProperties != null || r2dbcPoolProperties != null ) {
            "A general pool configuration or a writer-specific pool configuration is required."
        }
    }

    @Primary
    @Bean
    fun r2dbcDatabase(): R2dbcDatabase {
        return writerDataSourceProperties
            .makeConnectionFactory(mySqlTcpProperties, mysqlDnsResolver)
            .let {
                val pooledConnectionFactory = when {
                    writerPoolProperties != null -> it.makePool(writerPoolProperties)
                    r2dbcPoolProperties != null -> it.makePool(r2dbcPoolProperties)
                    else -> throw IllegalArgumentException("A general pool configuration or a writer-specific pool configuration is required.")
                }

                makeExposedR2dbc(pooledConnectionFactory, r2DbcConfigurationProperties.dialect)
            }
    }
}