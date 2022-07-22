package soft.commons.component

import io.lettuce.core.cluster.RedisClusterClient
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection
import io.lettuce.core.codec.StringCodec
import org.springframework.stereotype.Component

@Component
class RedisClusterConnection(redisClusterClient: RedisClusterClient?) {
    final val redisConnection: StatefulRedisClusterConnection<String, String>? =
        redisClusterClient?.connect(StringCodec.UTF8)

    final val created: Boolean =
        redisClusterClient != null
}