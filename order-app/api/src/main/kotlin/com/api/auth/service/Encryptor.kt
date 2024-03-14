package com.api.auth.service

interface Encryptor {
    fun encrypt(password: String): String
    fun matches(rawPassword: String, encodedPassword: String): Boolean
}