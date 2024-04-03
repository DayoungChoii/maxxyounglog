package com.consumer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.PropertySource

const val BASE_PACKAGE = "com"

@SpringBootApplication(scanBasePackages = [BASE_PACKAGE])
@PropertySource("classpath:/secure-aws.properties", "classpath:/secure-jwt.properties")
class ConsumerApplication

fun main(args: Array<String>) {
	runApplication<ConsumerApplication>(*args)
}
