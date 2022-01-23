package soft.commons.component

import org.springframework.stereotype.Component
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient

/**
 * 다른 프로젝트에서 Config 에 접근시, bean을 찾을 수 없다는 경고가 발생.!
 *  - 현재까지 리서치한 내용으로는 @Configuration 인에 있는 @Bean이 이와 같이 에러가 발생.
 *  - 다른 프로젝트에서 SpringBootApplication 하위에 @ComponentScan(basePackages=["soft.commons"] 를 넣어도 되나,
 *  - 이렇게 할 경우 SpringBootApplication에 선언된 Bean 을 등록하지 못함
 *  - basePackages=["soft.*"]를 사용하면, 문제와 동일한 현상이 발생하기 때문에
 *  - @Configuration 을 Wrapping 한 @Component 구성
 */
@Component
class DynamoDbConnector(
    private val dynamoDbAsyncClient: DynamoDbAsyncClient?
) {
    fun getAsyncClient(): DynamoDbAsyncClient? = dynamoDbAsyncClient
}