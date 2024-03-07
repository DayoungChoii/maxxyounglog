package com.api.auth.exception

import com.api.auth.status.AuthExceptionMessage

class AuthException (
    message: AuthExceptionMessage,
    e: Throwable?
): RuntimeException(message.name, e)