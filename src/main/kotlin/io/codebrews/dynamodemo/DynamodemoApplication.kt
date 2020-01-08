package io.codebrews.dynamodemo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DynamodemoApplication

fun main(args: Array<String>) {
	runApplication<DynamodemoApplication>(*args)
}
