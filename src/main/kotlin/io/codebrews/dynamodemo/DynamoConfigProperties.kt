package io.codebrews.dynamodemo

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "application.dynamo")
data class DynamoConfigProperties(
    val customerTableName: String,
    val region: String,
    val endpoint: String
)
