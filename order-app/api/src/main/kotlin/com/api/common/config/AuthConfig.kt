package com.api.common.config

import com.api.auth.service.Encryptor
import com.api.auth.service.SecurityEncryptor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AuthConfig {

    @Bean
    fun encryptor():Encryptor {
        return SecurityEncryptor()
    }
}