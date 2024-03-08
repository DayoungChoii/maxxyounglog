package com.api.common.exception

import org.springframework.http.HttpStatusCode

class ExceptionResult (
    val code: HttpStatusCode,
    val message: String
)