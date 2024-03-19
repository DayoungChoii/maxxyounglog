package com.api.auth.filter

import com.api.auth.constant.SECRET_KEY
import com.api.auth.dto.UserAuthentication
import com.api.auth.exception.AuthExceptionType.EXPIRED_TOKEN
import com.api.auth.exception.AuthExceptionType.INVALID_TOKEN
import io.jsonwebtoken.*
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter


class JwtAuthorizationFilter: OncePerRequestFilter() {

    companion object {
        val log: Logger = LoggerFactory.getLogger(this::class.java)
    }
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val accessToken = request.getHeader(HttpHeaders.AUTHORIZATION)

        accessToken?.let {
            if (validateToken(accessToken)) {
                val parseClaimsJws = parseJwtToken(accessToken)
                val userId = parseClaimsJws!!.body.subject
                SecurityContextHolder.getContext().authentication = UserAuthentication(userId)
            }
        }

        filterChain.doFilter(request, response)
    }

    private fun validateToken(token: String): Boolean {
        try {
            parseJwtToken(token)
            return true
        } catch (e: ExpiredJwtException) {
            log.info(EXPIRED_TOKEN.message)
        } catch (e: JwtException) {
            log.info(INVALID_TOKEN.message)
        }
        return false;
    }

    private fun parseJwtToken(token: String): Jws<Claims>? {
        return Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token)
    }
}