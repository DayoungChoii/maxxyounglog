package com.api.auth.service

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

class SecurityEncryptor():Encryptor {
    override fun encrypt(password: String): String {
        val passwordEncoder = BCryptPasswordEncoder()
        return passwordEncoder.encode(password)
    }
}