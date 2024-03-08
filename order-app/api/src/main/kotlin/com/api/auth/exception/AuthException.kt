package com.api.auth.exception

import com.api.common.exception.CustomException

class AuthException (
    type: AuthExceptionType,
    e: Throwable?
): CustomException(type, e)