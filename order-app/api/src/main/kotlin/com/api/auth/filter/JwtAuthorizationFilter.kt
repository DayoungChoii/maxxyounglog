package com.api.auth.filter

import com.api.auth.JwtTokenHelper
import com.api.auth.constant.ACCESS_SECRET_KEY
import com.api.auth.dto.UserAuthentication
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
            if (JwtTokenHelper.validateToken(accessToken, ACCESS_SECRET_KEY)) {
                val parseClaimsJws = JwtTokenHelper.parseJwtToken(accessToken, ACCESS_SECRET_KEY)
                val userId = parseClaimsJws!!.body.subject
                SecurityContextHolder.getContext().authentication = UserAuthentication(userId)
            }
        }
        filterChain.doFilter(request, response)
    }


}