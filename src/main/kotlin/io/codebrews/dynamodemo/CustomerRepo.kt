package io.codebrews.dynamodemo

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient
import software.amazon.awssdk.enhanced.dynamodb.Key
import software.amazon.awssdk.enhanced.dynamodb.TableSchema

@Repository
class CustomerRepo(private val client: DynamoDbEnhancedAsyncClient,
                   @Value("\${application.dynamo.customer-table-name}") private val customerTableName: String) {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    private val table = client.table(customerTableName, tableSchema)

    fun saveCustomer(customer: CustomerPersist): Mono<Unit> {
        return Mono.fromFuture(table.putItem(customer))
            .map { Unit }
            .doOnError { logger.error("Exception while saving Customer information - $it") }
    }

    fun retrieveCustomer(customerId: String): Mono<CustomerPersist> {
        val key = Key.builder().partitionValue(customerId).build()

        return Mono.fromFuture(table.getItem(key))
            .doOnError { logger.error("Exception while retrieving Customer information - $it") }
    }

    companion object {
        private val tableSchema = TableSchema.fromBean(CustomerPersist::class.java)
    }
}
