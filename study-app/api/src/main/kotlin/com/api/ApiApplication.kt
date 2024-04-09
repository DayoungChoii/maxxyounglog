package com.api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.PropertySource

const val BASE_PACKAGE = "com"

@EnableCaching
@SpringBootApplication(scanBasePackages = [BASE_PACKAGE])
@PropertySource("classpath:/secure-aws.properties", "classpath:/secure-jwt.properties")
class ApiApplication

fun main(args: Array<String>) {
	runApplication<ApiApplication>(*args)
}
