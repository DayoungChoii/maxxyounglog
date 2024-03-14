package com.api.auth.service

import com.api.auth.dto.LogInRequest
import com.api.auth.dto.LogInResponse
import com.api.auth.status.LogInStatus.*
import com.rds.user.domain.User
import com.rds.user.repository.UserRepository
import com.redis.auth.domain.Session
import com.redis.auth.repository.SessionRepository

class SessionLoginServiceImpl(
    private val encryptor: Encryptor,
    private val userRepository: UserRepository,
    private val sessionRepository: SessionRepository

): LoginService {
    override fun login(loginRequest: LogInRequest): LogInResponse {
        val user = userRepository.findByEmail(loginRequest.email) ?: return LogInResponse(INVALID_EMAIL)
        if (isPasswordInvalid(loginRequest, user)){
            return LogInResponse(INVALID_PASSWORD)
        }

        val savedSession = sessionRepository.save(Session(user.id))
        return LogInResponse(SUCCESS, savedSession.token)
    }

    private fun isPasswordInvalid(loginRequest: LogInRequest, user: User) =
        !encryptor.matches(loginRequest.password, user.password)

    override fun logout() {
    }
}