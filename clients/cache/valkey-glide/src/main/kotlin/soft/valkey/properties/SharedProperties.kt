package soft.soft.valkey.properties

import glide.api.models.configuration.BackoffStrategy
import glide.api.models.configuration.IamAuthConfig
import glide.api.models.configuration.NodeAddress
import glide.api.models.configuration.ServerCredentials
import soft.soft.valkey.enums.CacheServiceType
import soft.soft.valkey.enums.toServiceType

data class Address(
    val host: String,
    val port: Int,
) {
    fun toNodeAddress(): NodeAddress =
        NodeAddress.builder()
            .host(host)
            .port(port)
            .build()
}

data class Credential(
    val username: String,
    val password: String? = null,
    val iam: IamCredential? = null,
) {
    init {
        if((password == null && iam == null) || (password != null && iam != null)) {
            throw IllegalArgumentException("must use either a password or IAM configuration")
        }
    }

    data class IamCredential(
        val region: String,
        val clusterName: String,
        val cacheServiceType: CacheServiceType
    ) {
        fun toIamCredential(): IamAuthConfig =
            IamAuthConfig.builder()
                .region(region)
                .clusterName(clusterName)
                .service(cacheServiceType.toServiceType())
                .build()
    }

    fun toServerCredentials(): ServerCredentials {
        val builder = ServerCredentials.builder()
            .username(username)

        if (!password.isNullOrBlank()) {
            builder.password(password)
        }

        if (iam != null) {
            builder.iamConfig(iam.toIamCredential())
        }

        return builder.build()
    }
}

data class ConnectionRetryStrategy(
    /**
     * Retry 횟수
     */
    val maxRetries: Int = 5,
    /**
     * backOff 지수 증가 Base
     * exponentBase^(retry 횟수) 로 계산됨
     * e.g. amxRetry = 2 인 경우
     *  - 0 -> 2^0 = 1
     *  - 1 -> 2^1 = 2
     *  - 2 -> 2^3 = 8
     */
    val exponentBase: Int = 2,
    /**
     * bacOff 지수 결정값에 지연 시간 (default - 100ms)
     * 재시도 1회 = 100 * (2^1) = 200ms
     */
    val backoffMs: Int = 100,
) {
    fun toBackoffStrategy() = BackoffStrategy.builder()
        .numOfRetries(maxRetries)
        .exponentBase(exponentBase)
        .jitterPercent(2)
        .factor(backoffMs)
        .build()
}

