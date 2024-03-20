package com.api.auth.filter

import com.api.auth.JwtTokenHelper
import com.api.auth.constant.LogInStatus
import com.api.auth.dto.LogInRequest
import com.api.auth.dto.LogInResponse
import com.google.gson.Gson
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.io.InputStreamReader
import java.util.concurrent.TimeUnit

class JwtAuthenticationFilter(
    private val authenticationManager: AuthenticationManager,
    private val redisTemplate: RedisTemplate<String, String>,
    private val accessExpirationTime: Long,
    private val refreshExpirationTime: Long
) : UsernamePasswordAuthenticationFilter() {

    init {
        setFilterProcessesUrl("/v1/login")
    }

    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {
        try {
            val gson = Gson()
            val loginRequest: LogInRequest = gson.fromJson(InputStreamReader(request.inputStream, Charsets.UTF_8), LogInRequest::class.java)
            val authenticationToken = UsernamePasswordAuthenticationToken(loginRequest.email, loginRequest.password)
            return authenticationManager.authenticate(authenticationToken)
        } catch (e: Exception) {
            throw e
        }
    }

    override fun successfulAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain,
        authResult: Authentication
    ) {
        val userDetails = authResult.principal as UserDetails
        val accessToken = JwtTokenHelper.createAccessToken(userDetails.username, accessExpirationTime)
        val refreshToken = JwtTokenHelper.createRefreshToken(userDetails.username, refreshExpirationTime)
        saveRefreshToken(userDetails.username, refreshToken)

        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.writer.write(Gson().toJson(LogInResponse(LogInStatus.SUCCESS, accessToken, refreshToken)))
        response.setHeader(HttpHeaders.AUTHORIZATION, accessToken)
    }

    private fun saveRefreshToken(email:String, refreshToken: String) {
        redisTemplate.opsForValue().set(
            email,
            refreshToken,
            refreshExpirationTime,
            TimeUnit.MILLISECONDS
        )
    }

    override fun unsuccessfulAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
        failed: AuthenticationException
    ) {
        throw failed
    }
}

