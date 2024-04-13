package com.api.common.exception

import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode

enum class RedisExceptionType(
    override val httpStatusCode: HttpStatusCode,
    override val message: String
) : CustomExceptionType {
    LOCK_TIME_OUT(HttpStatus.SERVICE_UNAVAILABLE, "fail to get redis lock")
}