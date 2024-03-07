package com.api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

const val BASE_PACKAGE = "com"

@ConfigurationPropertiesScan(basePackages = [BASE_PACKAGE])
@SpringBootApplication(scanBasePackages = [BASE_PACKAGE])
class ApiApplication

fun main(args: Array<String>) {
	runApplication<ApiApplication>(*args)
}
