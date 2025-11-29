package soft.soft.valkey.enums

import glide.api.models.configuration.ServiceType

enum class CacheServiceType {
    ELASTIC_CACHE,
    MEMORY_DB
}

internal fun CacheServiceType.toServiceType() =
    when (this) {
        CacheServiceType.ELASTIC_CACHE -> ServiceType.ELASTICACHE
        CacheServiceType.MEMORY_DB -> ServiceType.MEMORYDB
    }