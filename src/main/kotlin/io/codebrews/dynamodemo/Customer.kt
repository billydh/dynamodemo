package io.codebrews.dynamodemo

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*

data class Customer(val emailAddress: String, val firstName: String, val lastName: String)

@DynamoDbBean
data class CustomerPersist(
    @get:DynamoDbPartitionKey var customerId: String? = null,
    var emailAddress: String? = null,
    var firstName: String? = null,
    var lastName: String? = null
)
