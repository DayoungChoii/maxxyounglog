package com.api.auth.filter

import com.api.auth.constant.LogInStatus
import com.api.auth.constant.SECRET_KEY
import com.api.auth.dto.LogInRequest
import com.api.auth.dto.LogInResponse
import com.google.gson.Gson
import io.jsonwebtoken.Jwts
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.io.InputStreamReader
import java.util.*
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
        } catch (ex: Exception) {
            throw UsernameNotFoundException("Failed to authenticate user")
        }
    }

    override fun successfulAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain,
        authResult: Authentication
    ) {
        val userDetails = authResult.principal as UserDetails
        val accessToken = createAccessToken(userDetails.username)
        createRefreshToken(userDetails.username)
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.writer.write(Gson().toJson(LogInResponse(LogInStatus.SUCCESS, accessToken)))
        response.setHeader(HttpHeaders.AUTHORIZATION, accessToken)
    }

    override fun unsuccessfulAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
        failed: AuthenticationException
    ) {
        response.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.reasonPhrase)
    }

    private fun createAccessToken(userId: String): String {
        val claims = Jwts.claims().apply { subject = userId }
        val now = Date()
        val expiredDate = Date(now.time + accessExpirationTime)
        return Jwts.builder()
            .signWith(SECRET_KEY)
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(expiredDate)
            .compact()
    }

    private fun createRefreshToken(userId: String): String {
        val key = SECRET_KEY
        val claims = Jwts.claims().apply { subject = userId.toString() }
        val now = Date()
        val expiredDate = Date(now.time + refreshExpirationTime)

        val refreshToken = Jwts.builder()
            .signWith(key)
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(expiredDate)
            .compact()

        redisTemplate.opsForValue().set(
            userId.toString(),
            refreshToken,
            refreshExpirationTime,
            TimeUnit.MILLISECONDS
        )

        return refreshToken
    }
}

