package io.codebrews.dynamodemo

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient
import java.net.URI

@Configuration
class DynamoClientProperties(@Value("\${application.dynamo.region}") private val region: String,
                             @Value("\${application.dynamo.endpoint}") private val endpoint: String) {

    @Bean
    fun dynamoDbAsyncClient(): DynamoDbAsyncClient {
        return DynamoDbAsyncClient.builder()
            .region(Region.of(region))
            .endpointOverride(URI.create(endpoint))
            .credentialsProvider(DefaultCredentialsProvider.builder().build())
            .build()
    }
}
