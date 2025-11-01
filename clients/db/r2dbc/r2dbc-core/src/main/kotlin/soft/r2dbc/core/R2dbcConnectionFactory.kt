package soft.r2dbc.core

import io.asyncer.r2dbc.mysql.MySqlConnectionConfiguration
import io.asyncer.r2dbc.mysql.MySqlConnectionFactory
import io.asyncer.r2dbc.mysql.constant.SslMode
import io.r2dbc.pool.ConnectionPool
import io.r2dbc.pool.ConnectionPoolConfiguration
import io.r2dbc.spi.ConnectionFactory
import io.r2dbc.spi.ConnectionFactoryOptions
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.r2dbc.ConnectionFactoryBuilder
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions
import soft.r2dbc.core.config.MySqlDnsConfig.MysqlDnsResolver
import soft.r2dbc.core.converter.IntegerToBooleanConverter
import soft.r2dbc.core.converter.LongToBooleanConverter
import soft.r2dbc.core.enums.ConnectionFactoryType
import soft.r2dbc.core.enums.R2dbcDialect
import soft.r2dbc.core.properties.DataSourceProperties
import soft.r2dbc.core.properties.PoolProperties
import soft.r2dbc.core.properties.mysql.MySqlTcpProperties
import java.time.Duration

private const val CONNECTION_FACTORY_DRIVER = "mysql"
private val logger: Logger = LoggerFactory.getLogger("nest.r2dbc.core.R2dbcConnection")

// TODO: MYSQL, POSTGRES 등등으로 고쳐야 함. (h2 까지만 하면 될듯)
fun R2dbcDialect.customConversions(): R2dbcCustomConversions =
    R2dbcCustomConversions.of(
        dialect,
        ArrayList<Any>().apply {
            addAll(dialect.converters)
            addAll(R2dbcCustomConversions.STORE_CONVERTERS)
            add(IntegerToBooleanConverter())
            add(LongToBooleanConverter())
        }
    )

fun DataSourceProperties.makeConnectionFactory(
    mySqlTcpProperties: MySqlTcpProperties? = null,
    mysqlDnsCache: MysqlDnsResolver? = null,
): ConnectionFactory =
    when(factoryType) {
        ConnectionFactoryType.MYSQL ->
            this.makeMySQLConnectionFactory(mySqlTcpProperties)
        ConnectionFactoryType.SPI -> {
            if (mySqlTcpProperties != null) {
                logger.warn("mySqlTcpProperties can only be used with MySqlConnectionFactory. If you need mySqlTcp configuration, please use ConnectionFactoryType.MYSQL.")
            }
            if (mysqlDnsCache != null) {
                logger.warn("Custom DNS can only be used with MySqlConnectionFactory. If you need DNS configuration, please use ConnectionFactoryType.MYSQL.")
            }

            this.makeDefaultConnectionFactory()
        }
        else -> throw IllegalArgumentException("Connection factory type $factoryType not supported")
    }

fun DataSourceProperties.makeDefaultConnectionFactory(): ConnectionFactory =
    ConnectionFactoryBuilder.withOptions(
        ConnectionFactoryOptions.builder()
            .option(ConnectionFactoryOptions.DRIVER, CONNECTION_FACTORY_DRIVER)
            .option(ConnectionFactoryOptions.HOST, host)
            .option(ConnectionFactoryOptions.SSL, ssl)
            .option(ConnectionFactoryOptions.PORT, port)
            .option(ConnectionFactoryOptions.DATABASE, database)
            .option(ConnectionFactoryOptions.USER, username)
            .option(ConnectionFactoryOptions.PASSWORD, password)
    ).build()


fun DataSourceProperties.makeMySQLConnectionFactory(
    mySqlTcpProperties: MySqlTcpProperties? = null,
    mysqlDnsCache: MysqlDnsResolver? = null
): ConnectionFactory {
    return MySqlConnectionFactory.from(
        MySqlConnectionConfiguration.builder()
            .host(this.host)
            .port(this.port)
            .user(this.username)
            .password(this.password)
            .database(this.database)
            .sslMode(if (this.ssl) SslMode.PREFERRED else SslMode.DISABLED)
            .also { builder ->
                mySqlTcpProperties?.apply {
                    builder.tcpNoDelay(tcpNoDelay)
                    builder.tcpKeepAlive(tcpKeepAlive)
                } ?: logger.warn("If MySqlTcpProperties is not configured, OLTP performance degradation may occur.")

                mysqlDnsCache?.apply {
                    builder.resolver(addressResolver)
                }
            }
            .build()
    )
}

fun ConnectionFactory.makePool(poolProperties: PoolProperties): ConnectionFactory {
    return ConnectionPool(
        ConnectionPoolConfiguration.builder(this)
            .initialSize(poolProperties.initialSize)
            .maxSize(poolProperties.maxSize)
            .minIdle(poolProperties.minIdle)
            .maxLifeTime(Duration.ofMillis(poolProperties.maxLifeTime))
            .maxCreateConnectionTime(Duration.ofMillis(poolProperties.maxCreateConnectionTime))
            .maxIdleTime(Duration.ofMillis(poolProperties.maxIdleTime))
            .validationQuery(poolProperties.validationQuery)
            .maxValidationTime(Duration.ofMillis(poolProperties.validationTimeout))
            .maxAcquireTime(Duration.ofMillis(poolProperties.maxAcquireTime))
            .acquireRetry(poolProperties.acquireRetry)
            .also { builder ->
                if (poolProperties.evictionInterval != null) {
                    builder.backgroundEvictionInterval(Duration.ofMillis(poolProperties.evictionInterval!!))
                }
            }
            .build()
    ).apply {
        if (poolProperties.warmup) {
            logger.info("R2dbcPool warmup has been executed. (async#Publisher.subscribe())")
            warmup().subscribe()
        }
    }
}