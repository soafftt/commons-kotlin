package soft.commons.component

import io.lettuce.core.cluster.RedisClusterClient
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection
import io.lettuce.core.codec.StringCodec
import org.springframework.stereotype.Component

@Component
open class RedisClusterConnection(private val redisClusterClient: RedisClusterClient?) {
    var redisConnection: StatefulRedisClusterConnection<String, String>? = null
        protected set;

    var created: Boolean = false
        protected set;

    init {
        if (redisClusterClient != null) {
            this.redisConnection = redisClusterClient.connect(StringCodec.UTF8)
            this.created = true
        }
    }
}