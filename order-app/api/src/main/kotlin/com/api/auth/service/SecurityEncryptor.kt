package com.api.auth.service

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

class SecurityEncryptor():Encryptor {

    private val passwordEncoder = BCryptPasswordEncoder()
    override fun encrypt(password: String): String {
        return passwordEncoder.encode(password)
    }

    override fun matches(rawPassword: String, encodedPassword: String): Boolean {
        return passwordEncoder.matches(rawPassword, encodedPassword)
    }
}