package soft.r2dbc.write

import io.r2dbc.spi.ConnectionFactory
import org.jetbrains.exposed.v1.r2dbc.R2dbcDatabase
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.DependsOn
import org.springframework.context.annotation.Primary
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
@ConditionalOnProperty(value = ["r2dbc.datasource.writer"])
class WriterConnectionFactoryConfig(
    private val r2DbcConfigurationProperties: R2dbcConfigurationProperties,
    private val writerDataSourceProperties: WriterDataSourceProperties,
    private val writerPoolProperties: WriterPoolProperties,
    @param:Autowired(required = false) private val mySqlTcpProperties: MySqlTcpProperties? = null,
    @param:Autowired(required = false) private val mysqlDnsResolver: MysqlDnsResolver? = null
) : AbstractR2dbcConfiguration() {

    @Primary
    @Bean
    @DependsOn("connectionFactory")
    fun readerR2dbcEntityOperations(connectionFactory: ConnectionFactory): R2dbcEntityOperations {
        val databaseClient: DatabaseClient = DatabaseClient.create(connectionFactory)
        return R2dbcEntityTemplate(databaseClient, MySqlDialect.INSTANCE)
    }

    @Primary
    @Bean
    override fun r2dbcConverter(
        mappingContext: R2dbcMappingContext,
        r2dbcCustomConversions: R2dbcCustomConversions
    ): MappingR2dbcConverter {
        return super.r2dbcConverter(mappingContext, r2dbcCustomConversions)
    }


    @Primary
    @Bean
    override fun r2dbcCustomConversions(): R2dbcCustomConversions {
        return r2DbcConfigurationProperties.dialect.customConversions()
    }

    @Primary
    @Bean(value = ["readerTransactionManager"])
    @DependsOn("connectionFactory")
    fun readTransactionManager(connectionFactory: ConnectionFactory): ReactiveTransactionManager {
        return R2dbcTransactionManager(connectionFactory)
    }

    @Primary
    @Bean
    override fun connectionFactory(): ConnectionFactory {
        return writerDataSourceProperties
            .makeConnectionFactory(mySqlTcpProperties, mysqlDnsResolver)
            .makePool(writerPoolProperties)
    }

    @ConditionalOnProperty(value = ["r2dbc.implementation"], havingValue = R2dbcImplementation.USE_EXPOSED)
    @Primary
    @Bean
    fun r2dbcDatabase(connectionFactory: ConnectionFactory): R2dbcDatabase {
        return makeExposedR2dbc(connectionFactory, r2DbcConfigurationProperties.dialect)
    }
}