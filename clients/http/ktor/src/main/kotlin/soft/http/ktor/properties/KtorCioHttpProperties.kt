package soft.http.ktor.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "http.ktor.cio")
class CioHttpProperties(
    val requestTimeout: Long = 15_000,
    val maxConnectionsCount: Int = 1000,
    val pipelining: Boolean = false,
    /**
     * dispatchers 에서 thread 분리의 복적.
     *  * Dispatchers.IO 를 쓸수는 있으나, thread 공유 상태로 인하여 분리의 복적
     *  * useVtDispatcher = true 인 경우 무시됨
     */
    val cioDispatchers: KtorDispatcherProperties? = null,
    /**
     * virtualThread Dispatcher 사용여부
     */
    val useVtDispatcher: Boolean = false,
    val endpoint: CIOEnginEndpointProperties = CIOEnginEndpointProperties()
)

data class CIOEnginEndpointProperties(
    val connectTimeout: Long = 10000L,
    /**
     * 설정하지 않으면 알아서 처리.
     *  * 다만 한쪽에서 고갈시키지 않도록 제어 하는 것이 좋으며 host 당 100 이 default 입니다.
     */
    val maxConnectionsPerRoute: Int? = null, // default 100
    /**
     * client 에서 연결 이후 evict 하기 위한 connection aliveTime
     *  default: 초
     */
    val keepAliveTime: Long = 5000L,
    /**
     * http/2 의 multi plex 개념
     * http/1.1 의 HOL Blocking 으로 인하여 높은 값은 추천하지 않음
     */
    val pipelineMaxSize: Int = 4,
    val connectAttempts: Int = 1,
    /**
     * Request / Response 의 응답 대기 시간
     *  * default 10000ms
     */
    val socketTimeout: Long = 1000L
)