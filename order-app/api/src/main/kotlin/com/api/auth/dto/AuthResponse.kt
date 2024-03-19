package com.api.auth.dto

import com.api.auth.constant.LogInStatus

data class LogInResponse (
    val status: LogInStatus,
    val token: String = ""
)