package io.codebrews.dynamodemo

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router

@Configuration
class Routes(private val customerHandler: CustomerHandler) {
    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    @Bean
    fun route(): RouterFunction<ServerResponse> = router {
        ("/users").nest {
            accept(MediaType.APPLICATION_JSON).nest {
                POST("", customerHandler::saveCustomerInformation)
            }

            GET("/{customerId}", customerHandler::retrieveCustomerInformation)
        }
    }
}
