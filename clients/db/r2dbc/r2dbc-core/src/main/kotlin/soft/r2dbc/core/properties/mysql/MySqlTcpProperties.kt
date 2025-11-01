package soft.r2dbc.core.properties.mysql

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * ConnectionFactoryType.MYSQL 일때만 사용하는 옵션으로, 쿼리 전송에 대한 tcp 설정
 */
@ConfigurationProperties("r2dbc.mysql.tcp")
data class MySqlTcpProperties(
    /**
     * 쿼리 전송시 일정 쿼리를 모아서 전송하는 것을 사용하지 않고, 모든 쿼리에 대해서 전송 옵션
     *  - default: true
     *  - false 인경우 일정기간 쿼리를 보아서 한번에 전송하기 때문에 대량 쿼리에 유용
     */
    val tcpNoDelay: Boolean = true,
    /**
     * 유휴 TCP 연결을 확인하고 끊김 방지 : OS lv 설정을 따릅니다.
     */
    val tcpKeepAlive: Boolean = true
)