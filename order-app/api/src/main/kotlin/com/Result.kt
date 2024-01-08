package com

class StatusResult<S> (
    val status: S
)

class DataResponse<S, T>(
    val status: S,
    val data: T
)