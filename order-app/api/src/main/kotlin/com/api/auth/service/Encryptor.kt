package com.api.auth.service

interface Encryptor {
    fun encrypt(password: String): String
}