package soft.r2dbc.read

import io.r2dbc.spi.ConnectionFactory
import org.jetbrains.exposed.v1.r2dbc.R2dbcDatabase
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.DependsOn
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration
import org.springframework.data.r2dbc.convert.MappingR2dbcConverter
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions
import org.springframework.data.r2dbc.core.R2dbcEntityOperations
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.r2dbc.dialect.MySqlDialect
import org.springframework.data.r2dbc.mapping.R2dbcMappingContext
import org.springframework.r2dbc.connection.R2dbcTransactionManager
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.transaction.ReactiveTransactionManager
import soft.r2dbc.core.config.MySqlDnsConfig
import soft.r2dbc.core.config.MySqlDnsConfig.MysqlDnsResolver
import soft.r2dbc.core.customConversions
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
@ConditionalOnProperty(value = ["r2dbc.datasource.reader"])
class ReaderConnectionFactoryConfig(
    private val r2DbcConfigurationProperties: R2dbcConfigurationProperties,
    private val readerDataSourceProperties: ReaderDataSourceProperties,
    private val readerPoolProperties: ReaderPoolProperties,
    @param:Autowired(required = false) private val mySqlTcpProperties: MySqlTcpProperties? = null,
    @param:Autowired(required = false) private val mysqlDnsResolver: MysqlDnsResolver? = null
) : AbstractR2dbcConfiguration() {

    @Bean
    @DependsOn("readerConnectionFactory")
    fun readerR2dbcEntityOperations(@Qualifier("readerConnectionFactory") connectionFactory: ConnectionFactory): R2dbcEntityOperations {
        val databaseClient: DatabaseClient = DatabaseClient.create(connectionFactory)
        return R2dbcEntityTemplate(databaseClient, MySqlDialect.INSTANCE)
    }

    @Bean
    override fun r2dbcConverter(
        mappingContext: R2dbcMappingContext,
        r2dbcCustomConversions: R2dbcCustomConversions
    ): MappingR2dbcConverter {
        return super.r2dbcConverter(mappingContext, r2dbcCustomConversions)
    }


    @Bean
    override fun r2dbcCustomConversions(): R2dbcCustomConversions {
        return r2DbcConfigurationProperties.dialect.customConversions()
    }

    @Bean(value = ["readerTransactionManager"])
    @DependsOn("readerConnectionFactory")
    fun readTransactionManager(@Qualifier("readerConnectionFactory") connectionFactory: ConnectionFactory): ReactiveTransactionManager {
        return R2dbcTransactionManager(connectionFactory).apply {
            this.isEnforceReadOnly = true
        }
    }

    @Bean("readerConnectionFactory")
    override fun connectionFactory(): ConnectionFactory {
        return readerDataSourceProperties
            .makeConnectionFactory(mySqlTcpProperties, mysqlDnsResolver)
            .makePool(readerPoolProperties)
    }

    @Bean("readerR2dbcDatabase")
    @DependsOn("readerConnectionFactory")
    fun r2dbcDatabase(@Qualifier("readerConnectionFactory") connectionFactory: ConnectionFactory): R2dbcDatabase {
        return makeExposedR2dbc(connectionFactory, r2DbcConfigurationProperties.dialect)
    }
}