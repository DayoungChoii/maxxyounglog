package com.api.auth.service

import com.api.auth.dto.LogInResponse
import com.api.auth.dto.LogInRequest

interface LoginService {

    fun login(request: LogInRequest): LogInResponse
    fun logout()
}