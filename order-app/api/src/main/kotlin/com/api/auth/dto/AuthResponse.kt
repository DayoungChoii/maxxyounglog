package com.api.auth.dto

import com.api.auth.status.LogInStatus

data class LogInResponse (
    val status: LogInStatus,
    val sessionToken: String = ""
)