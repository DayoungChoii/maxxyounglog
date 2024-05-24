package com.crm.common

class StatusResult<S> (
    val status: S
)

class DataResult<T> (
    val data: T
)

class StatusDataResult<T, U> (
    val status: T,
    val data: U
)