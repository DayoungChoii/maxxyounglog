package com.batch

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.PropertySource
import org.springframework.scheduling.annotation.EnableScheduling

const val BASE_PACKAGE = "com"

@EnableScheduling
@PropertySource("classpath:/secure-aws.properties")
@SpringBootApplication(scanBasePackages = [BASE_PACKAGE])
class BatchApplication

fun main(args: Array<String>) {
	runApplication<BatchApplication>(*args)

}
