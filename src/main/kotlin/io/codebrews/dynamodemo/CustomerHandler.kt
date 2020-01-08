package io.codebrews.dynamodemo

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.json
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono
import java.util.*

@Component
class CustomerHandler(private val customerRepo: CustomerRepo) {
    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    fun saveCustomerInformation(request: ServerRequest): Mono<ServerResponse> {
        val customerId: String = generateCustomerId()

        return request.bodyToMono(Customer::class.java)
            .map { CustomerPersist(customerId, it.emailAddress, it.firstName, it.lastName) }
            .flatMap { customerRepo.saveCustomer(it) }
            .flatMap { ServerResponse.ok().json().bodyValue("{\"customerId\": \"$customerId\"}") }
            .doOnError { logger.error("Exception while trying to store a new customer record - $it") }
    }

    fun retrieveCustomerInformation(request: ServerRequest): Mono<ServerResponse> {

        return Mono.fromSupplier { request.pathVariable("customerId") }
            .flatMap { customerRepo.retrieveCustomer(it) }
            .flatMap { ServerResponse.ok().json().body(Mono.just(it), CustomerPersist::class.java) }
            .doOnError { logger.error("Exception while trying to retrieve a customer record - $it") }
    }

    private fun generateCustomerId(): String {
        return UUID.randomUUID().toString()
    }
}
