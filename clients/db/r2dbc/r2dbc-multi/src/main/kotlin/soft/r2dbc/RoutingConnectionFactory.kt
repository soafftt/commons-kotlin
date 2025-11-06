package soft.r2dbc

import org.springframework.r2dbc.connection.lookup.AbstractRoutingConnectionFactory
import reactor.core.publisher.Mono

const val R2DBC_ROUTING_KEY = "R2DBC_ROUTING_KEY"

class RoutingConnectionFactory : AbstractRoutingConnectionFactory() {
    override fun determineCurrentLookupKey(): Mono<in Any> {
        return Mono.deferContextual { context ->
            if (context.hasKey(R2DBC_ROUTING_KEY)) {
                Mono.just(context.get(R2DBC_ROUTING_KEY))
            } else {
                Mono.error(RuntimeException("R2DBC_ROUTING_KEY is not defined"))
            }
        }
    }
}