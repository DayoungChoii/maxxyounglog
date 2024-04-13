package com.api.common.exception

class RedisException(
    type: RedisExceptionType,
    e: Throwable?
): CustomException(type, e) {
    constructor(type: RedisExceptionType): this(type, null)
}