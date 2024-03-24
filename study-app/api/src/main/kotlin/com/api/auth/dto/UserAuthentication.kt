package com.api.auth.dto

import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority

class UserAuthentication(
    private val userId: String
): Authentication {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf()
    }

    override fun setAuthenticated(isAuthenticated: Boolean) {
    }

    override fun getName(): String {
        return userId
    }

    override fun getCredentials(): Any {
        return Any()
    }

    override fun getDetails(): Any {
        return Any()
    }

    override fun getPrincipal(): Any {
        return userId
    }

    override fun isAuthenticated(): Boolean {
        return true
    }
}