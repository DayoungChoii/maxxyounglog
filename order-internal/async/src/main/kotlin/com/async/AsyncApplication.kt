package com.async

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AsyncApplication

fun main(args: Array<String>) {
	runApplication<AsyncApplication>(*args)
}
