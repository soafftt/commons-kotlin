package soft.r2dbc.core.config

import io.netty.channel.EventLoop
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioDatagramChannel
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.resolver.AddressResolver
import io.netty.resolver.AddressResolverGroup
import io.netty.resolver.InetSocketAddressResolver
import io.netty.resolver.dns.DefaultDnsCache
import io.netty.resolver.dns.DefaultDnsCnameCache
import io.netty.resolver.dns.DnsNameResolver
import io.netty.resolver.dns.DnsNameResolverBuilder
import io.netty.util.concurrent.EventExecutor
import org.springframework.beans.factory.DisposableBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import soft.r2dbc.core.properties.mysql.MySqlDnsCacheProperties
import java.net.InetSocketAddress

@EnableConfigurationProperties(MySqlDnsCacheProperties::class)
@Configuration
@ConditionalOnProperty(name = ["r2dbc.mysql.dns"])
class MySqlDnsConfig(
    private val mySQLDnsCacheProperties: MySqlDnsCacheProperties
) {
    @Bean
    fun mysqlDnsCache(): MysqlDnsResolver =
        with(mySQLDnsCacheProperties) {
            val dnsCache = DefaultDnsCache(minTtl, maxTtl, negativeTtl)
            val cNameCache = DefaultDnsCnameCache(minTtl, maxTtl)
            val nioEventLoop = NioEventLoopGroup(ioCount).next()

            val dnsNameResolver = DnsNameResolverBuilder(nioEventLoop)
                .resolveCache(dnsCache)
                .cnameCache(cNameCache)
                .queryTimeoutMillis(queryTimeoutMillis)       // dnsLookup timeout mills
                .socketChannelFactory { NioSocketChannel() }
                .datagramChannelFactory { NioDatagramChannel() }
                .build()

            MysqlDnsResolver(
                eventLoop = nioEventLoop,
                dnsCache = dnsCache,
                dnsCNameCache = cNameCache,
                addressResolver = MySqlAddressResolverGroup(dnsNameResolver)
            )
        }

    @ConsistentCopyVisibility
    data class MysqlDnsResolver internal constructor(
        private val eventLoop: EventLoop,
        internal val dnsCache: DefaultDnsCache,
        internal val dnsCNameCache: DefaultDnsCnameCache,
        val addressResolver: AddressResolverGroup<InetSocketAddress>,
    ) : DisposableBean {
        override fun destroy() {
            eventLoop.shutdownGracefully().await()
        }
    }

    internal class MySqlAddressResolverGroup(
        private val mysqlDnsNameResolver: DnsNameResolver
    ) : AddressResolverGroup<InetSocketAddress>() {
        override fun newResolver(eventExecutor: EventExecutor?): AddressResolver<InetSocketAddress> {
            return InetSocketAddressResolver(eventExecutor, mysqlDnsNameResolver)
        }
    }
}