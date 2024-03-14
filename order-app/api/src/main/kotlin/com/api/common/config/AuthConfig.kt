package com.api.common.config

import com.api.auth.service.Encryptor
import com.api.auth.service.LoginService
import com.api.auth.service.SecurityEncryptor
import com.api.auth.service.SessionLoginServiceImpl
import com.rds.user.repository.UserRepository
import com.redis.auth.repository.SessionRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
internal class AuthConfig (
    private val userRepository: UserRepository,
    private val sessionRepository: SessionRepository
) {
    @Bean
    fun encryptor():Encryptor {
        return SecurityEncryptor()
    }

    @Bean
    fun loginService(): LoginService{
        return SessionLoginServiceImpl(encryptor(), userRepository, sessionRepository)
    }
}