package soft.commons.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient
import java.net.URI

@SuppressWarnings("SpringFacetCodeInspection")
@Configuration
@Component
class DynamoDBConfig(
    @Value("\${database.dynamodb.endpoint:}")
    private val endPoint: String,

    @Value("\${database.dynamodb.region:}")
    private val region: String,

    @Value("\${database.dynamodb.access-key:}")
    private val accessKey: String,

    @Value("\${database.dynamodb.secret-key:}")
    private val secretKey: String,
) {

    @Bean
    fun dynamoDbAsyncClient(): DynamoDbAsyncClient? = when {
        endPoint.isNotEmpty() -> with(
            DynamoDbAsyncClient.builder()
                .endpointOverride(URI.create(this.endPoint))
                .region(Region.of(region))
        ) {
            if (accessKey.isNotEmpty() || secretKey.isNotEmpty()) {
                credentialsProvider { AwsBasicCredentials.create(accessKey, secretKey) }
            }

            return build()
        }
        else -> null
    }
}