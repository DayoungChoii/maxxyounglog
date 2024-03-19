package com.api.auth.service.security

import com.api.auth.service.Encryptor
import org.springframework.context.annotation.PropertySource
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import java.util.*

@Component
@PropertySource("classpath:/secure-jwt.properties")
class JwtTokenProvider (
    private val userDetailsService: UserDetailsService,
    private val encryptor: Encryptor
): AuthenticationProvider {
    override fun authenticate(authentication: Authentication): Authentication {
        val username = authentication.name
        val password = authentication.credentials.toString()

        val userDetails = userDetailsService.loadUserByUsername(username)
        if(!encryptor.matches(password, userDetails.password)){
            throw BadCredentialsException("Invalid Password")
        }

        return UsernamePasswordAuthenticationToken(userDetails, password, userDetails.authorities)
    }

    override fun supports(authentication: Class<*>): Boolean {
        return authentication == UsernamePasswordAuthenticationToken::class.java
    }
}