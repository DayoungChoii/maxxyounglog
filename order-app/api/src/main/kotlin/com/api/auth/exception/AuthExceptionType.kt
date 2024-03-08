package com.api.auth.exception

import com.api.common.exception.CustomExceptionType
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode

enum class AuthExceptionType (
    override val httpStatusCode: HttpStatusCode,
    override val message: String
): CustomExceptionType {
    SIGN_UP_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "sign up fail");
}