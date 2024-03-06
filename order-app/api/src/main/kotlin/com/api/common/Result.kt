package com.api.common

class StatusResult<S> (
    val status: S
)

class DataStatusResult<S, T> (
    val status: S,
    val data: T
)

class DataResult<T> (
    val data: T
)