package io.codebrews.dynamodemo

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient
import software.amazon.awssdk.services.dynamodb.model.AttributeValue
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest
import software.amazon.awssdk.services.dynamodb.model.PutItemResponse
import java.util.*

@Repository
class CustomerRepo(private val client: DynamoDbAsyncClient,
                   @Value("\${application.dynamo.customer-table-name}") private val customerTableName: String) {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    fun saveCustomer(customer: CustomerPersist): Mono<PutItemResponse> {
        val putItemRequest = PutItemRequest.builder()
            .item(
                mapOf(
                    "customerId" to AttributeValue.builder().s(customer.customerId).build(),
                    "emailAddress" to AttributeValue.builder().s(customer.emailAddress).build(),
                    "firstName" to AttributeValue.builder().s(customer.firstName).build(),
                    "lastName" to AttributeValue.builder().s(customer.lastName).build()
                )
            )
            .tableName(customerTableName)
            .build()

        return Mono.fromFuture(client.putItem(putItemRequest))
            .doOnError { logger.error("Exception while saving Customer information - $it") }
    }

    fun retrieveCustomer(customerId: String): Mono<CustomerPersist> {
        val getItemRequest = GetItemRequest.builder()
            .key(
                mapOf(
                    "customerId" to AttributeValue.builder().s(customerId).build()
                )
            )
            .tableName(customerTableName)
            .build()

        return Mono.fromFuture(client.getItem(getItemRequest))
            .map { response ->
                CustomerPersist(
                    customerId,
                    (response.item()["emailAddress"] ?: error("emailAddress N/A")).s(),
                    (response.item()["firstName"] ?: error("firstName N/A")).s(),
                    (response.item()["lastName"] ?: error("lastName N/A")).s())
            }
            .doOnError { logger.error("Exception while retrieving Customer information - $it") }
    }
}
